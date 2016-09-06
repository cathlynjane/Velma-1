package velmalatest.garciano.com.velmalatest.apiclient;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;

/**
 * Created by Cathlyn Jane Amodia on 8/30/2016.
 */
public interface MyJsonService {

    @GET("/1kpjf")
    void listEvents(Callback<List<Event>> eventsCallback);
}
