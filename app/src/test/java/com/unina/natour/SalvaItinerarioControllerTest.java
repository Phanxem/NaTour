package com.unina.natour;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import android.location.Address;

import com.unina.natour.controllers.ResultMessageController;
import com.unina.natour.controllers.SalvaItinerarioController;
import com.unina.natour.dto.request.SaveItineraryRequestDTO;
import com.unina.natour.dto.response.GetGpxResponseDTO;
import com.unina.natour.dto.response.GetItineraryResponseDTO;
import com.unina.natour.dto.response.GetListItineraryResponseDTO;
import com.unina.natour.dto.response.composted.GetItineraryWithGpxAndUserAndReportResponseDTO;
import com.unina.natour.dto.response.composted.GetListItineraryWithUserResponseDTO;
import com.unina.natour.models.AddressModel;
import com.unina.natour.models.SalvaItinerarioModel;
import com.unina.natour.models.dao.interfaces.ItineraryDAO;
import com.unina.natour.views.activities.NaTourActivity;
import com.unina.natour.views.dialogs.MessageDialog;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class SalvaItinerarioControllerTest {
    private SalvaItinerarioController salvaItinerarioController;

    private NaTourActivity activity;
    private ResultMessageController resultMessageController;
    private SalvaItinerarioModel salvaItinerarioModel;
    private ItineraryDAO itineraryDAO;

    @Before
    public void setUp(){

        activity = mock(NaTourActivity.class);


        resultMessageController = mock(ResultMessageController.class);

        MessageDialog messageDialog = new MessageDialog();
        messageDialog.setMessage("test");
        when(resultMessageController.getMessageDialog())
                .thenReturn(messageDialog);

        when(resultMessageController.getActivity())
                .thenReturn(activity);


        salvaItinerarioModel = mock(SalvaItinerarioModel.class);

        when(salvaItinerarioModel.getDifficulty())
                .thenReturn(0);

        when(salvaItinerarioModel.getDistance())
                .thenReturn(300.0f);

        when(salvaItinerarioModel.getDuration())
                .thenReturn(500.0f);

        when(salvaItinerarioModel.getDefaultDuration())
                .thenReturn(600.0f);

        AddressModel addressModel1 = mock(AddressModel.class);
        AddressModel addressModel2 = mock(AddressModel.class);
        List<AddressModel> wayPoints = new ArrayList<AddressModel>();
        wayPoints.add(addressModel1);
        wayPoints.add(addressModel2);
        when(salvaItinerarioModel.getWayPoints())
                .thenReturn(wayPoints);


        itineraryDAO = mock(ItineraryDAO.class);

        GetItineraryResponseDTO getItineraryResponseDTO = mock(GetItineraryResponseDTO.class);
        when(getItineraryResponseDTO.getResultMessage())
                .thenReturn(ResultMessageController.SUCCESS_MESSAGE);
        when(itineraryDAO.getItineraryById(anyLong()))
                .thenReturn(getItineraryResponseDTO);

        GetGpxResponseDTO getGpxResponseDTO = mock(GetGpxResponseDTO.class);
        when(getGpxResponseDTO.getResultMessage())
                .thenReturn(ResultMessageController.SUCCESS_MESSAGE);
        when(itineraryDAO.getItineraryGpxById(anyLong()))
                .thenReturn(getGpxResponseDTO);

        GetListItineraryResponseDTO getListItineraryResponseDTO = mock(GetListItineraryResponseDTO.class);
        when(getListItineraryResponseDTO.getResultMessage())
                .thenReturn(ResultMessageController.SUCCESS_MESSAGE);
        when(itineraryDAO.getListItineraryByIdUser(anyLong(), anyInt()))
                .thenReturn(getListItineraryResponseDTO);

        when(itineraryDAO.getListItineraryByName(anyString(), anyInt()))
                .thenReturn(getListItineraryResponseDTO);

        when(itineraryDAO.getListItineraryRandom())
                .thenReturn(getListItineraryResponseDTO);

        GetListItineraryWithUserResponseDTO getListItineraryWithUserResponseDTO = mock(GetListItineraryWithUserResponseDTO.class);
        when(getListItineraryWithUserResponseDTO.getResultMessage())
                .thenReturn(ResultMessageController.SUCCESS_MESSAGE);
        when(itineraryDAO.getListItineraryWithUserByName(anyString(),anyInt()))
                .thenReturn(getListItineraryWithUserResponseDTO);

        when(itineraryDAO.getListItineraryWithUserRandom())
                .thenReturn(getListItineraryWithUserResponseDTO);

        GetItineraryWithGpxAndUserAndReportResponseDTO getItineraryWithGpxAndUserAndReportResponseDTO = mock(GetItineraryWithGpxAndUserAndReportResponseDTO.class);
        when(getItineraryWithGpxAndUserAndReportResponseDTO.getResultMessage())
                .thenReturn(ResultMessageController.SUCCESS_MESSAGE);
        when(itineraryDAO.getItineraryWithGpxAndUserAndReportById(anyLong()))
                .thenReturn(getItineraryWithGpxAndUserAndReportResponseDTO);

        when(itineraryDAO.addItinerary(any()))
                .thenReturn(ResultMessageController.SUCCESS_MESSAGE);

        when(itineraryDAO.deleteItineraryById(anyLong()))
                .thenReturn(ResultMessageController.SUCCESS_MESSAGE);

        salvaItinerarioController = new SalvaItinerarioController(activity,resultMessageController,salvaItinerarioModel,itineraryDAO);
    }

    //Test Case 1 ---
    @Test
    public void testInitModelWithDurationNegativeAndDistancePositiveAndWayPointsSize2(){
        float duration = -1.0f;
        float distance = 300.0f;
        List<AddressModel> wayPoints = mock(List.class);
        when(wayPoints.size())
                .thenReturn(2);

        boolean result = salvaItinerarioController.initModel(duration, distance, wayPoints);

        assertFalse(result);
    }

    //Test Case 2 ---
    @Test
    public void testInitModelWithDurationPositiveAndDistanceNegativeAndWayPointsSize2(){
        float duration = 500.0f;
        float distance = -1.0f;
        List<AddressModel> wayPoints = mock(List.class);
        when(wayPoints.size())
                .thenReturn(2);

        boolean result = salvaItinerarioController.initModel(duration, distance, wayPoints);

        assertFalse(result);
    }

    //Test Case 3 ---
    @Test
    public void testInitModelWithDurationPositiveAndDistancePositiveAndWayPointsNull(){
        float duration = 500.0f;
        float distance = 300.0f;
        List<AddressModel> wayPoints = null;

        boolean result = salvaItinerarioController.initModel(duration, distance, wayPoints);

        assertFalse(result);
    }

    //Test Case 4 ---
    @Test
    public void testInitModelWithDurationPositiveAndDistancePositiveAndWayPointsSize1(){
        float duration = 500.0f;
        float distance = 300.0f;
        List<AddressModel> wayPoints = mock(List.class);
        when(wayPoints.size())
                .thenReturn(1);

        boolean result = salvaItinerarioController.initModel(duration, distance, wayPoints);

        assertFalse(result);
    }

    //Test Case 5 ---
    @Test
    public void testInitModelWithDurationPositiveAndDistancePositiveAndWayPointsSize2(){
        float duration = 500.0f;
        float distance = 300.0f;
        List<AddressModel> wayPoints = mock(List.class);
        when(wayPoints.size())
                .thenReturn(2);

        boolean result = salvaItinerarioController.initModel(duration, distance, wayPoints);

        assertTrue(result);
    }
}
