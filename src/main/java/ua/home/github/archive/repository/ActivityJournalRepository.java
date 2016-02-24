package ua.home.github.archive.repository;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Set;

@Repository
public class ActivityJournalRepository {

    public static final String COLLECTION_NAME = "activities";
    public static final String FIELD_NAME = "date";
    @Resource(name="db")
    private DB mongo;

    public Set<String> getActivities() {
        Set<String> result = new HashSet<>();
        DBCursor activities = mongo.getCollection(COLLECTION_NAME).find();
        while(activities.hasNext()) {
            result.add( (String) activities.next().get(FIELD_NAME));
        }
        return result;
    }

    public void addActivity(String date) {
        DBObject jsonObj = new BasicDBObject(FIELD_NAME, date);
        mongo.getCollection(COLLECTION_NAME).insert(jsonObj);
    }
}
