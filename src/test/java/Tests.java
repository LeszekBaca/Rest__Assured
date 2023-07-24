import io.restassured.http.ContentType;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class Tests {

    @Test
    public void showSearchTest() {

        given().get("https://api.tvmaze.com/search/shows?q=girls")
                .then().statusCode(200)
                .body("$", notNullValue());
    }

    @Test
    public void showSingleSearchTest() {

        given().get("https://api.tvmaze.com/singlesearch/shows?q=girls")
                .then().statusCode(200)
                .body("name", equalTo("Girls"));
    }

    @Test
    public void showLookupTest() {

        given()
                .get("https://api.tvmaze.com/shows/{id}", "81189")
                .then()
                .statusCode(404)
                .statusCode(not(equalTo(301)))
                .statusCode(not(equalTo(200)));
    }

    @Test
    public void peopleSearchTest() {

        given()
                .queryParam("q", "lauren")
                .get("https://api.tvmaze.com/search/people")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("person.name", hasItem(containsString("Lauren")));
    }

    @Test
    public void scheduleTest() {

        given()
                .queryParam("country", "US")
                .queryParam("date", "2014-12-01")
                .get("https://api.tvmaze.com/schedule")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("show.network.country.code", hasItem("US"));
    }

    @Test
    public void streamingScheduleTest() {

        given()
                .queryParam("date", "2020-05-29")
                .queryParam("country", "US")
                .get("https://api.tvmaze.com/schedule/web")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("_embedded.show.network.country.code", hasItem("US"));
    }

    @Test
    public void fullScheduleTest() {

        given()
                .get("https://api.tvmaze.com/schedule/full")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("size()", greaterThan(0));
    }

    @Test
    public void showMainInformationTest() {

        given()
                .get("https://api.tvmaze.com/shows/1?embed=cast")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("_embedded.cast", notNullValue());

    }

    @Test
    public void showEpisodeListTest() {

        given()
                .get("https://api.tvmaze.com/shows/1/episodes?specials=1")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("name", hasItems("Pilot", "The Fire", "Manhunt"));

    }

    @Test
    public void showAlternateListsTest() {

        given()
                .get("https://api.tvmaze.com/alternatelists/1/alternateepisodes?embed=episodes")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("name", hasItems("Serenity", "The Train Job", "Bushwhacked"))
                .body("name", notNullValue());

    }

    @Test
    public void episodeByNumberTest() {

        given()
                .get("https://api.tvmaze.com/shows/1/episodesbydate?date=2013-07-01")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("airdate", everyItem(equalTo("2013-07-01")));
    }

    @Test
    public void showSeasonsTest() {

        given()
                .get("https://api.tvmaze.com/shows/1/seasons")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("id", hasItems(1, 2, 6233));
    }

    @Test
    public void showCastTest() {

        given()
                .get("https://api.tvmaze.com/shows/1/cast")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("person.name", hasItems("Mike Vogel", "Rachelle Lefevre", "Alexander Koch"))
                .body("character.name", hasItems("Dale \"Barbie\" Barbara", "Julia Shumway"));
    }

    @Test
    public void showCrewTest() {

        given()
                .get("https://api.tvmaze.com/shows/1/crew")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("type", hasItems("Creator", "Executive Producer"))
                .body("person.name", hasItems("Stephen King", "Steven Spielberg", "Jack Bender", "Stacey Snider", "Neal Baer"));
    }

    @Test
    public void showAKATest() {

        given()
                .get("https://api.tvmaze.com/shows/1/akas")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("name", hasItems("Под куполом", "A búra alatt", "Под купола"))
                .body("country.name", hasItems("Russian Federation", "Hungary", "Bulgaria"))
                .body("country.code", hasItems("RU", "HU", "BG"));
    }

    @Test
    public void showIndexTest() {

        given()
                .get("https://api.tvmaze.com/shows?page=1")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("id", containsInRelativeOrder(250, 499));

    }

    @Test
    public void episodeMainInformationTest() {

        given()
                .get("https://api.tvmaze.com/episodes/1?embed=show")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("id", equalTo(1))
                .body("url", containsString("/episodes/1"))
                .body("name", equalTo("Pilot"))
                .body("season", equalTo(1))
                .body("number", equalTo(1))
                .body("type", equalTo("regular"))
                .body("airdate", equalTo("2013-06-24"))
                .body("airtime", equalTo("22:00"))
                .body("airstamp", equalTo("2013-06-25T02:00:00+00:00"))
                .body("runtime", equalTo(60))
                .body("rating.average", equalTo(6.8f))
                .body("image.medium", containsString("/medium_landscape/1/4388.jpg"))
                .body("image.original", containsString("/original_untouched/1/4388.jpg"))
                .body("summary", containsString("<p>When the residents of Chester's Mill find themselves trapped under a massive transparent dome with no way out, they struggle to survive as resources rapidly dwindle and panic quickly escalates.</p>"))
                .body("_links.self.href", equalTo("https://api.tvmaze.com/episodes/1"))
                .body("_links.show.href", equalTo("https://api.tvmaze.com/shows/1"))
                .body("_embedded.show.id", equalTo(1))
                .body("_embedded.show.url", containsString("/shows/1/under-the-dome"))
                .body("_embedded.show.name", equalTo("Under the Dome"))
                .body("_embedded.show.type", equalTo("Scripted"))
                .body("_embedded.show.language", equalTo("English"))
                .body("_embedded.show.genres", contains("Drama", "Science-Fiction", "Thriller"))
                .body("_embedded.show.status", equalTo("Ended"))
                .body("_embedded.show.runtime", equalTo(60))
                .body("_embedded.show.averageRuntime", equalTo(60))
                .body("_embedded.show.premiered", equalTo("2013-06-24"))
                .body("_embedded.show.ended", equalTo("2015-09-10"))
                .body("_embedded.show.officialSite", equalTo("http://www.cbs.com/shows/under-the-dome/"))
                .body("_embedded.show.schedule.time", equalTo("22:00"))
                .body("_embedded.show.schedule.days", contains("Thursday"))
                .body("_embedded.show.rating.average", equalTo(6.5f))
                .body("_embedded.show.weight", equalTo(99))
                .body("_embedded.show.network.id", equalTo(2))
                .body("_embedded.show.network.name", equalTo("CBS"))
                .body("_embedded.show.network.country.name", equalTo("United States"))
                .body("_embedded.show.network.country.code", equalTo("US"))
                .body("_embedded.show.network.country.timezone", equalTo("America/New_York"))
                .body("_embedded.show.network.officialSite", equalTo("https://www.cbs.com/"))
                .body("_embedded.show.webChannel", nullValue())
                .body("_embedded.show.dvdCountry", nullValue())
                .body("_embedded.show.externals.tvrage", equalTo(25988))
                .body("_embedded.show.externals.thetvdb", equalTo(264492))
                .body("_embedded.show.externals.imdb", equalTo("tt1553656"))
                .body("_embedded.show.image.medium", containsString("/medium_portrait/81/202627.jpg"))
                .body("_embedded.show.image.original", containsString("/original_untouched/81/202627.jpg"))
                .body("_embedded.show.summary", containsString("<p><b>Under the Dome</b> is the story of a small town that is suddenly and inexplicably sealed off from the rest of the world by an enormous transparent dome. The town's inhabitants must deal with surviving the post-apocalyptic conditions while searching for answers about the dome, where it came from and if and when it will go away.</p>"))
                .body("_embedded.show.updated", equalTo(1631010933))
                .body("_embedded.show._links.self.href", equalTo("https://api.tvmaze.com/shows/1"))
                .body("_embedded.show._links.previousepisode.href", equalTo("https://api.tvmaze.com/episodes/185054"));
    }

    @Test
    public void episodeGuestCastTest() {

        given()
                .get("https://api.tvmaze.com/episodes/1/guestcast")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("$", notNullValue())
                .body("person.name", hasItems("Mackenzie Lintz", "Dale Raoul", "Samantha Mathis"))
                .body("character.name", hasItems("Norrie Calvert-Hill", "Andrea Grinnell", "Alice Calvert"));
    }

    @Test
    public void episodeQuestCrewTest() {

        given()
                .get("https://api.tvmaze.com/episodes/1/guestcrew")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("$", notNullValue())
                .body("person.name", hasItems("Stephen King", "Brian K. Vaughan", "Niels Arden Oplev"))
                .body("guestCrewType", hasItems("Story", "Teleplay", "Director"));
    }

    @Test
    public void personMainInformationTest() {

        given()
                .get("https://api.tvmaze.com/people/1?embed=castcredits")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("$", notNullValue())
                .body("name", equalTo("Mike Vogel"))
                .body("country.name", equalTo("United States"))
                .body("gender", equalTo("Male"))
                .body("birthday", equalTo("1979-07-17"))
                .body("_embedded.castcredits.self", hasSize(greaterThan(0)));
    }

    @Test
    public void personCastCreditsTest() {

        given()
                .get("https://api.tvmaze.com/people/1/castcredits?embed=show")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("$", notNullValue())
                .body("$", hasSize(greaterThan(0)))
                .body("_embedded.show", hasSize(greaterThan(0)));
    }

    @Test
    public void showUpdatesTest() {

        given()
                .get("https://api.tvmaze.com/updates/shows?since=day")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("$", notNullValue())
                .body("size()", greaterThan(0));
    }

    @Test
    public void personUpdates() {

        given()
                .get("https://api.tvmaze.com/updates/people?since=day")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("$", notNullValue())
                .body("size()", greaterThan(0));
    }
}













