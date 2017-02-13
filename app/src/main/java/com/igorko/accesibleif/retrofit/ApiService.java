package com.igorko.accesibleif.retrofit;

/**
 * Created by Igorko on 10.10.2016.
 */

import com.igorko.accesibleif.models.Data;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {

    //TODO
    /*@GET
    Call<Data> getData(@Url String url);

    @GET("api/interpreter")
    Call<Data> getDataWithQuery(@Query("data") String data);
    */

    /*
    [out:json]
    [bbox:48.8614907,24.6424252,48.9567578,24.7589659];
    (node[wheelchair];way[wheelchair];relation[wheelchair];);
    out geom;
    */
    @GET("api/interpreter?data=%5Bout%3Ajson%5D%0A%20%20%20%20%5Bbbox%3A48.8614907%2C24.6424252%2C48.9567578%2C24.7589659%5D%3B%0A%20%20%20%20(node%5Bwheelchair%5D%3Bway%5Bwheelchair%5D%3Brelation%5Bwheelchair%5D%3B)%3B%0A%20%20%20%20out%20geom%3B")
    Call <Data> getAllBuildings();

    /*
    [out:json]
    [bbox:48.8614907,24.6424252,48.9567578,24.7589659];
    (node[wheelchair]["amenity"="pharmacy"];way[wheelchair]["amenity"="pharmacy"];relation[wheelchair]["amenity"="pharmacy"];);
    out geom;
     */
    @GET("api/interpreter?data=%5Bout%3Ajson%5D%0A%20%20%20%20%5Bbbox%3A48.8614907%2C24.6424252%2C48.9567578%2C24.7589659%5D%3B%0A%20%20%20%20(node%5Bwheelchair%5D%5B%22amenity%22%3D%22pharmacy%22%5D%3Bway%5Bwheelchair%5D%5B%22amenity%22%3D%22pharmacy%22%5D%3Brelation%5Bwheelchair%5D%5B%22amenity%22%3D%22pharmacy%22%5D%3B)%3B%0A%20%20%20%20out%20geom%3B")
    Call <Data> getPharmacyBuildings();

    /*
    [out:json]
    [bbox:48.8614907,24.6424252,48.9567578,24.7589659];
    (node[wheelchair]["amenity"="hospital"];way[wheelchair]["amenity"="hospital"];relation[wheelchair]["amenity"="hospital"];);
    out geom;
     */
    @GET("api/interpreter?data=%20%20%5Bout%3Ajson%5D%0A%20%20%20%20%5Bbbox%3A48.8614907%2C24.6424252%2C48.9567578%2C24.7589659%5D%3B%0A%20%20%20%20(node%5Bwheelchair%5D%5B%22amenity%22%3D%22hospital%22%5D%3Bway%5Bwheelchair%5D%5B%22amenity%22%3D%22hospital%22%5D%3Brelation%5Bwheelchair%5D%5B%22amenity%22%3D%22hospital%22%5D%3B)%3B%0A%20%20%20%20out%20geom%3B")
    Call <Data> getHospitalBuildings();

    /*
    [out:json]
    [bbox:48.8614907,24.6424252,48.9567578,24.7589659];
    (node[wheelchair]["shop"];way[wheelchair]["shop"];relation[wheelchair]["shop"];);
    out geom;
    */
    @GET("api/interpreter?data=%5Bout%3Ajson%5D%0A%5Bbbox%3A48.8614907%2C24.6424252%2C48.9567578%2C24.7589659%5D%3B%0A(node%5Bwheelchair%5D%5B%22shop%22%5D%3Bway%5Bwheelchair%5D%5B%22shop%22%5D%3Brelation%5Bwheelchair%5D%5B%22shop%22%5D%3B)%3B%0Aout%20geom%3B%0A")
    Call <Data> getShopBuildings();

    /*
    [out:json]
    [bbox:48.8614907,24.6424252,48.9567578,24.7589659];
    (node[wheelchair]["amenity"="atm"];way[wheelchair]["amenity"="atm"];relation[wheelchair]["amenity"="atm"];);
    out geom;
    */
    @GET("api/interpreter?data=%5bout%3ajson%5d%0d%0a++++%5bbbox%3a48.8614907%2c24.6424252%2c48.9567578%2c24.7589659%5d%3b%0d%0a++++(node%5bwheelchair%5d%5b%22amenity%22%3d%22atm%22%5d%3bway%5bwheelchair%5d%5b%22amenity%22%3d%22atm%22%5d%3brelation%5bwheelchair%5d%5b%22amenity%22%3d%22atm%22%5d%3b)%3b%0d%0a++++out+geom%3b")
    Call <Data> getATMBuildings();
}