package ua.home.github.archive.repository;

import com.mongodb.*;
import com.mongodb.util.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ua.home.github.archive.util.DateUtil;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Repository
public class EventRepository  {

    public static final String COLLECTION_NAME = "events";
    @Resource(name="db")
    private DB mongo;

    public void save(List<String> records) {
        for(String record : records) {
            DBObject obj = (DBObject) JSON.parse(record);
            obj.put("created_at", DateUtil.parseGithubDate(obj.get("created_at").toString()));
            mongo.getCollection(COLLECTION_NAME).save(obj);
        }
    }

    public List<String> findTop5Events(LocalDateTime from, LocalDateTime to) {
        return getTop5(from, to, "type");
    }

    private List<String> getTop5(LocalDateTime from, LocalDateTime to, String field) {
        DBObject projectFields = new BasicDBObject(field, 1);
        DBObject project = new BasicDBObject("$project", projectFields );

        DBObject groupFields = new BasicDBObject( "_id", "$" + field);
        groupFields.put("count", new BasicDBObject("$sum", 1));
        DBObject group = new BasicDBObject("$group", groupFields);
        DBObject limit = new BasicDBObject("$limit", 5);
        DBObject sort = new BasicDBObject("$sort", new BasicDBObject("count", -1));
        DBObject match = new BasicDBObject("$match",
                    new BasicDBObject("created_at", //
                        new BasicDBObject("$gte", DateUtil.toDate(from)).append("$lt", DateUtil.toDate(to)))
        );

        AggregationOutput aggregate = mongo.getCollection(COLLECTION_NAME)
                .aggregate(Arrays.asList(match, group, sort, limit, project));

        List<String> result = collectResults(aggregate);
        return result;
    }

    private List<String> collectResults(AggregationOutput aggregate) {
        List<String> result = new ArrayList<>();
        for (DBObject obj : aggregate.results()) {
            result.add((String) obj.get("_id"));
        }
        return result;
    }

    public List<String> findTop5Users(LocalDateTime from, LocalDateTime to) {
        return getTop5(from, to, "actor.login");
    }

    public List<String> findTop5Repos(LocalDateTime from, LocalDateTime to) {
        return getTop5(from, to, "repo.name");
    }

    public long findTotalEventNumber(LocalDateTime from, LocalDateTime to) {
        DBObject query = new BasicDBObject("created_at", //
                new BasicDBObject("$gte", DateUtil.toDate(from)).append("$lt", DateUtil.toDate(to)));
        return mongo.getCollection(COLLECTION_NAME).count(query);
    }
}
