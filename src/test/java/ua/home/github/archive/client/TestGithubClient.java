package ua.home.github.archive.client;

import org.junit.Test;
import org.mockito.internal.util.reflection.Whitebox;
import ua.home.github.archive.service.GithubClient;

import java.util.List;


public class TestGithubClient {
    @Test
    public void testGetArchiveForPeriod() {
        GithubClient client = createClient();
        List<String> result = client.getRecords("2015-01-16-12");
        System.out.println();
    }

    private GithubClient createClient() {
        GithubClient client = new GithubClient();
        Whitebox.setInternalState(client, "githubUrl", "http://data.githubarchive.org/");
        return client;
    }


}
