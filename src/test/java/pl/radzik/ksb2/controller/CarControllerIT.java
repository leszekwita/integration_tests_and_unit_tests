package pl.radzik.ksb2.controller;

import org.flywaydb.core.Flyway;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@AutoConfigureMockMvc
@ActiveProfiles("test")
@SpringBootTest
public class CarControllerIT {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    Flyway flyway;

    @AfterEach
    void cleanDataBase() {
        flyway.clean();
        flyway.migrate();
    }

    @Test
    void shouldReturnAllCars() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/cars"))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Is.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].mark", Is.is("Fiat")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].model", Is.is("Seicento")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].color", Is.is("blue")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id", Is.is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].mark", Is.is("Opel")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].model", Is.is("Meriva")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].color", Is.is("white")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].id", Is.is(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].mark", Is.is("Mercedes")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].model", Is.is("ML350")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].color", Is.is("yellow")))
                .andDo(MockMvcResultHandlers.print());
    }


    @Test
    void shouldReturnCarWhenCarByIdAndIdEqualThree() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/cars/3"))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Is.is(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.mark", Is.is("Mercedes")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.model", Is.is("ML350")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.color", Is.is("yellow")))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void shoulReturnCarsWithSelectedColorWhenGetCarByColor() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/cars/color/{color}", "yellow"))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Is.is(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].mark", Is.is("Mercedes")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].model", Is.is("ML350")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].color", Is.is("yellow")))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void shouldAddCarWhenAddCar() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post("/cars")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        "\"mark\":\"Peugeot\"," +
                        "\"model\":\"207\"," +
                        "\"color\":\"blue\"" +
                        "}"))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andDo(MockMvcResultHandlers.print());
        mockMvc.perform(MockMvcRequestBuilders.get("/cars/"))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.jsonPath("$[3].id", Is.is(4)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[3].mark", Is.is("Peugeot")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[3].model", Is.is("207")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[3].color", Is.is("blue")));
    }

    @Test
    void shouldModifyCarWhenModifyCar() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/cars")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        "\"id\": 2," +
                        "\"mark\": \"Subaru\"," +
                        "\"model\": \"Impreza\"," +
                        "\"color\": \"grey\"" +
                        "}"))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andDo(MockMvcResultHandlers.print());
        mockMvc.perform(MockMvcRequestBuilders.get("/cars/2"))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Is.is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.mark", Is.is("Subaru")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.model", Is.is("Impreza")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.color", Is.is("grey")))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void shouldRemoveCarWhenRemoveCar() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/cars/{id}", "1"))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void shouldModifiedPartialCar() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.patch("/cars/{id}/{attribute}/{value}", "1", "color", "green"))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andDo(MockMvcResultHandlers.print());

        mockMvc.perform(MockMvcRequestBuilders.get("/cars/1"))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Is.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.mark", Is.is("Fiat")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.model", Is.is("Seicento")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.color", Is.is("green")))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void shouldReturnStatus404WhenCarToModifyDoesNotExist() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/cars")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        "\"id\": 8," +
                        "\"mark\": \"Subaru\"," +
                        "\"model\": \"Impreza\"," +
                        "\"color\": \"grey\"" +
                        "}"))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    void shouldReturnStatus404WhenCarToPartialModifyDoesNotExist() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.patch("/cars/{id}/{attribute}/{value}", "9", "color", "green"))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void shouldReturnStatus404WhenCarToRemoveDoesNotExist() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/cars/{id}", "9"))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void shouldReturnStatus404WhenPathAddCarAndPathIsIncorrect() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post("/car")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        "\"mark23\":\"Peugeot\"," +
                        "\"model23\":\"207\"," +
                        "\"color23\":\"blue\"" +
                        "}"))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andDo(MockMvcResultHandlers.print());
    }
}
