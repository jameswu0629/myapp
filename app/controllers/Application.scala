package controllers

import play.api._
import play.api.mvc._
import play.api.Play.current

import java.io.File
import com.maxmind.geoip2.DatabaseReader
import java.net.InetAddress
import com.maxmind.geoip2.model.CityResponse
import com.maxmind.geoip2.record.Country
import com.maxmind.geoip2.record.City
import com.maxmind.geoip2.record.Location
import com.maxmind.geoip2.record.Postal

class Application extends Controller {

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def geoinfo = Action {

    val database:File = new File("%s/conf/GeoLite2-City.mmdb".format(Play.application.path))

    val reader:DatabaseReader = new DatabaseReader.Builder(database).build()

    val ipAddress:InetAddress = InetAddress.getByName("128.101.101.101")

    val response:CityResponse = reader.city(ipAddress)

    val country:Country   = response.getCountry()
    val city:City         = response.getCity()
    val location:Location = response.getLocation()
    val postal:Postal     = response.getPostal()
    
    /*
    Subdivision subdivision = response.getMostSpecificSubdivision();
    System.out.println(subdivision.getName());    // 'Minnesota'
    System.out.println(subdivision.getIsoCode()); // 'MN'
     */

    Ok("""{"city": "%s", "lat": "%s", "lon": "%s"}""".format(city.getName(), location.getLatitude(), location.getLongitude())).as("application/json")
  }
}
