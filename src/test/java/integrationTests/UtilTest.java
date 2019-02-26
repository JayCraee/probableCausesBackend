package integrationTests;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import partib.groupProject.probableCauses.backend.ProbableCausesApplication;
import partib.groupProject.probableCauses.backend.controller.QueryController;
import partib.groupProject.probableCauses.backend.controller.UtilController;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@RunWith(SpringRunner.class)
@WebMvcTest(UtilController.class)
@ContextConfiguration(classes = ProbableCausesApplication.class)
public class UtilTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetTables() throws Exception {
        String uri = "/util/tableNames";
        String result = mockMvc.perform(get(uri)).andReturn().getResponse().getContentAsString();

        assertEquals("[CRIMEDATA]", result, "Error: wrong table names");
    }

    @Test
    public void testGetColumns() throws Exception {
        String uri = "/util/columnNames/CRIMEDATA";
        String result = mockMvc.perform(get(uri)).andReturn().getResponse().getContentAsString();

        assertEquals("[PrimaryType, District, LocationDescription, Arrest, Location, Latitude, CommunityArea, CaseNumber, Beat, Date, Ward, FBICode, YCoordinate, UpdatedOn, Longitude, ID, Block, Description, XCoordinate, Domestic, IUCR, Year]",
                result, "Error: wrong columns returned");
    }

    @Test
    public void testGetNominalColumns() throws Exception {
        String uri = "/util/nominalColumnNames/CRIMEDATA";
        String result = mockMvc.perform(get(uri)).andReturn().getResponse().getContentAsString();

        assertEquals("[Date, Block, IUCR, PrimaryType, Description, LocationDescription, Arrest, Domestic, Beat, District, Ward, CommunityArea, FBICode, Year, UpdatedOn, Location]",
                result, "Error: wrong columns returned");
    }
}
