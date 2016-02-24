package ua.home.github.archive.repository;

import com.mongodb.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ua.home.github.archive.api.Task;
import ua.home.github.archive.api.TaskStatus;
import ua.home.github.archive.data.TaskImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class TaskRepository {

    public static final String COLLECTION_NAME = "tasks";
    @Autowired
    private DB db;

    public void create(TaskImpl task) {
        DBObject dbObject = new BasicDBObject();
        dbObject.put("id", task.getId());
        dbObject.put("from", task.getFrom());
        dbObject.put("to", task.getTo());
        dbObject.put("userId", task.getUserId());
        dbObject.put("status", task.getStatus().toString());
        db.getCollection(COLLECTION_NAME).insert(dbObject);
    }

    public Optional<String> getStatus(String id) {
        DBObject one = getDbObjectForTask(id);
        if (one != null) {
            return Optional.of(one.get("status").toString());
        }
        return Optional.empty();
    }

    private DBObject getDbObjectForTask(String id) {
        DBObject query = new BasicDBObject("id", id);
        return db.getCollection(COLLECTION_NAME).findOne(query);
    }

    public List<Task> getTasks(String userId) {
        List<Task> result = new ArrayList<>();

        DBObject query = new BasicDBObject("userId", userId);
        DBCursor cursor = db.getCollection(COLLECTION_NAME).find(query);

        while(cursor.hasNext()) {
            DBObject next = cursor.next();

            // TODO add normal convertion of cursor to object
            Task task = createTask(next);
            result.add(task);
        }

        return result;
    }

    private Task createTask(DBObject next) {
        return new TaskImpl((String) next.get("id"),
                                        (String) next.get("from"),
                                        (String) next.get("to"),
                                        (String) next.get("userId"),
                                        TaskStatus.valueOf((String) next.get("status")));
    }

    public void completeTask(String taskId, TaskStatus status) {
        DBCollection collection = db.getCollection(COLLECTION_NAME);
        DBObject query = new BasicDBObject("id", taskId);
        DBObject object = collection.findOne(query);
        object.put("status", status.toString());
        collection.update(query, object);
    }

    public Task getTask(String taskId) {
        DBObject dbObject = getDbObjectForTask(taskId);
        Task result = createTask(dbObject);
        return result;
    }
}
