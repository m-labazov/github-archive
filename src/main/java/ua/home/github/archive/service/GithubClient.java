package ua.home.github.archive.service;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.zip.GZIPInputStream;

@Service
public class GithubClient {

    @Value("${github.url}")
    private String githubUrl;
    private static final String REQUEST_SUFFIX = ".json.gz";

    public List<String> getRecords(String date) {
        HttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(githubUrl + date + REQUEST_SUFFIX);
        try {
            HttpResponse response1 = httpClient.execute(httpGet);
            return Arrays.asList(read(response1.getEntity().getContent()).split("\n"));
        } catch (IOException e) {
            // TODO process exception
            e.printStackTrace();
        }
        return null;
    }

    private String read(InputStream tBytes) throws IOException {
        GZIPInputStream  gzip = new GZIPInputStream(tBytes);
        StringBuffer  szBuffer = new StringBuffer ();
        byte  tByte [] = new byte [1024];
        while (true)
        {
            int  iLength = gzip.read (tByte, 0, 1024);
            if (iLength < 0) {
                break;
            }
            szBuffer.append (new String (tByte, 0, iLength));
        }
        return szBuffer.toString();
    }
}
