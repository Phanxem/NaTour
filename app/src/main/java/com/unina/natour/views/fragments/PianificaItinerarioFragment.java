package com.unina.natour.views.fragments;

import android.graphics.Paint;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.unina.natour.R;
import com.unina.natour.controllers.MainController;
import com.unina.natour.controllers.PianificaItinerarioController;
import com.unina.natour.controllers.SalvaItinerarioController;
import com.unina.natour.controllers.utils.DrawableUtils;
import com.unina.natour.controllers.utils.TimeUtils;
import com.unina.natour.models.AddressModel;
import com.unina.natour.models.RouteLegModel;
import com.unina.natour.models.PianificaItinerarioModel;
import com.unina.natour.views.activities.NaTourActivity;

import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.FolderOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.Polyline;

import java.util.ArrayList;
import java.util.List;

public class PianificaItinerarioFragment extends NaTourFragment {

    private MapView mapView;
    private Marker selectionMarker;
    private FolderOverlay wayPointMarkers;
    private ArrayList<Polyline> routeTracks;

    private PianificaItinerarioController pianificaItinerarioController;


    private PianificaItinerarioModel pianificaItinerarioModel;

    public static PianificaItinerarioFragment newInstance(Parcelable controller){
        PianificaItinerarioFragment pianificaFragment = new PianificaItinerarioFragment();

        Bundle args = new Bundle();
        args.putParcelable(MainController.KEY_CONTROLLER, controller);
        pianificaFragment.setArguments(args);

        return pianificaFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pianifica_itinerario, container, false);
        setFragmentView(view);

        Bundle args = getArguments();
        if(args != null){
            Parcelable parcelable = args.getParcelable(MainController.KEY_CONTROLLER);
            if(parcelable instanceof PianificaItinerarioController) {
                this.pianificaItinerarioController = (PianificaItinerarioController) parcelable;
            }
        }
        else{
            this.pianificaItinerarioController = new PianificaItinerarioController(getNaTourActivity());
        }


        pianificaItinerarioModel = pianificaItinerarioController.getModel();
        pianificaItinerarioModel.registerObserver(this);

        mapView = view.findViewById(R.id.InsertItinerary_mapView_mappa);
        pianificaItinerarioController.initMap(mapView);


        List<Overlay> overlays = mapView.getOverlays();

        selectionMarker = new Marker(mapView);
        //selectionMarker.setIcon(getContext().getDrawable(R.drawable.ic_selected_point));
        selectionMarker.setIcon(DrawableUtils.getBitmapWithText(getContext(), R.drawable.ic_selected_point,null));
        selectionMarker.setAnchor(Marker.ANCHOR_CENTER,Marker.ANCHOR_BOTTOM);
        overlays.add(selectionMarker);

        wayPointMarkers = new FolderOverlay();
        overlays.add(wayPointMarkers);

        routeTracks = null;

        ListView listView_puntiIntermedi = view.findViewById(R.id.InsertItinerary_listView_puntiIntermedi);
        pianificaItinerarioController.initItermediatePointsList(listView_puntiIntermedi);

        pianificaItinerarioController.initOsmdroidConfiguration();



        pressMenuIcon();
        pressStartingPointField();
        pressIconCancelStartingPoint();
        pressDestinationPointField();
        pressIconCancelDestinationPoint();
        pressAddIntermediatePoint();
        pressShowIntermediatePoints();
        pressHideIntermediatePoints();

        pressIconCancelPointSelectedOnMap();
        pressButtonSetAsStartingPoint();
        pressButtonSetAsDestinationPoint();
        pressButtonSetAsIntermediatePoint();
        pressButtonSave();

        update();

        return view;
    }




    public void pressMenuIcon(){
        View view = getFragmentView();
        ImageView imageView_iconMenu = view.findViewById(R.id.InsertItinerary_imageView_iconMenu);

        PopupMenu popupMenu = new PopupMenu(view.getContext(),imageView_iconMenu);
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu_pianifica_itinerario, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId() == R.id.PianificaItinerarioF_popupMenu_importaGPX){
                    pianificaItinerarioController.goToImportGPX();
                    return true;
                }
                else return false;
            }
        });

        imageView_iconMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupMenu.show();
            }
        });
    }


    public void pressStartingPointField() {
        View view = getFragmentView();
        RelativeLayout relativeLayout_puntoPartenza = view.findViewById(R.id.InsertItinerary_relativeLayout_puntoPartenza);
        relativeLayout_puntoPartenza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pianificaItinerarioController.selectStartingPoint();
            }
        });
    }

    public void pressIconCancelStartingPoint() {
        View view = getFragmentView();
        ImageView imageView_annullaPuntoPartenza = view.findViewById(R.id.InsertItinerary_imageView_iconClosePuntoPartenza);
        imageView_annullaPuntoPartenza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pianificaItinerarioController.cancelStartingPoint();
            }
        });
    }


    public void pressDestinationPointField() {
        View view = getFragmentView();
        RelativeLayout relativeLayout_puntoDestinazione = view.findViewById(R.id.InsertItinerary_relativeLayout_puntoDestinazione);
        relativeLayout_puntoDestinazione.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pianificaItinerarioController.selectDestinationPoint();
            }
        });
    }

    public void pressIconCancelDestinationPoint() {
        View view = getFragmentView();
        ImageView imageView_annullaPuntoDestinazione = view.findViewById(R.id.InsertItinerary_imageView_iconClosePuntoDestinzione);
        imageView_annullaPuntoDestinazione.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pianificaItinerarioController.cancelDestinationPoint();
            }
        });
    }


    public void pressAddIntermediatePoint() {
        View view = getFragmentView();
        RelativeLayout relativeLayout_aggiungiPuntoIntermedio = view.findViewById(R.id.InsertItinerary_relativeLayout_aggiungiPuntoIntermedio);
        relativeLayout_aggiungiPuntoIntermedio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pianificaItinerarioController.addIntermediatePoint();
            }
        });

    }

    public void pressShowIntermediatePoints() {
        View view = getFragmentView();
        RelativeLayout relativeLayout_mostraPuntiIntermedi = view.findViewById(R.id.InsertItinerary_relativeLayout_mostraPuntiIntermedi);
        relativeLayout_mostraPuntiIntermedi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showItermediatePoints();
            }
        });
    }

    public void pressHideIntermediatePoints() {
        View view = getFragmentView();
        RelativeLayout relativeLayout_nascondiPuntiIntermedi = view.findViewById(R.id.InsertItinerary_relativeLayout_nascondiPuntiIntermedi);
        relativeLayout_nascondiPuntiIntermedi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideIntermediatePoints();
            }
        });
    }


    public void pressIconCancelPointSelectedOnMap() {
        View view = getFragmentView();
        ImageView imageView_annullaPuntoSelezionatoDaMappa = view.findViewById(R.id.InsertItinerary_imageView_iconCloseSelectedPoint);
        imageView_annullaPuntoSelezionatoDaMappa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pianificaItinerarioController.cancelPointSelectedOnMap();
            }
        });
    }

    public void pressButtonSetAsStartingPoint() {
        View view = getFragmentView();
        Button button_impostaPuntoPartenza = view.findViewById(R.id.InsertItinerary_button_impostaPuntoPartenza);
        button_impostaPuntoPartenza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pianificaItinerarioController.setSelectedPointOnMapAsStartingPoint();
            }
        });
    }

    public void pressButtonSetAsDestinationPoint() {
        View view = getFragmentView();
        Button button_impostaPuntoDestinazione = view.findViewById(R.id.InsertItinerary_button_impostaPuntoDestinazione);
        button_impostaPuntoDestinazione.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pianificaItinerarioController.setSelectedPointOnMapAsDestinationPoint();
            }
        });
    }

    public void pressButtonSetAsIntermediatePoint() {
        View view = getFragmentView();
        Button button_impostaPuntoIntermedio = view.findViewById(R.id.InsertItinerary_button_impostaPuntoIntermedio);
        button_impostaPuntoIntermedio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pianificaItinerarioController.setSelectedPointOnMapAsIntermediatePoint();
            }
        });
    }


    public void pressButtonSave() {
        View view = getFragmentView();
        NaTourActivity activity = getNaTourActivity();
        Button button_save = view.findViewById(R.id.InsertItinerary_button_salva);
        button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pianificaItinerarioController.goToSaveItinerary(pianificaItinerarioModel.getDuration(), pianificaItinerarioModel.getDistance(), (ArrayList<AddressModel>) pianificaItinerarioModel.getInterestPoints());
            }
        });
    }




//---

    private void showItermediatePoints(){
        View view = getFragmentView();
        ConstraintLayout constraintLayout_puntiIntermedi = view.findViewById(R.id.InsertItinerary_constraintLayout_puntiIntermedi);
        if(constraintLayout_puntiIntermedi.getVisibility() == View.VISIBLE) return;

        RelativeLayout relativeLayout_mostraPuntiIntermedi = view.findViewById(R.id.InsertItinerary_relativeLayout_mostraPuntiIntermedi);
        relativeLayout_mostraPuntiIntermedi.setVisibility(View.GONE);
        constraintLayout_puntiIntermedi.setVisibility(View.VISIBLE);
    }

    private void hideIntermediatePoints(){
        View view = getFragmentView();
        ConstraintLayout constraintLayout_puntiIntermedi = view.findViewById(R.id.InsertItinerary_constraintLayout_puntiIntermedi);
        if(constraintLayout_puntiIntermedi.getVisibility() != View.VISIBLE) return;

        RelativeLayout relativeLayout_mostraPuntiIntermedi = view.findViewById(R.id.InsertItinerary_relativeLayout_mostraPuntiIntermedi);
        relativeLayout_mostraPuntiIntermedi.setVisibility(View.VISIBLE);
        constraintLayout_puntiIntermedi.setVisibility(View.GONE);
    }



    @Override
    public void update() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                updateStartingPoint();
                updateDestinationPoint();

                updateOptionsIntermediatePoints();

                updateOptionsSelectFromMap();

                updateOptionsSaveItinerary();

                updateMapWithPointSelectedOnMapMarker();
                updateMapWithWaypointMarkers();
                updateMapWithRoutes();
                centerMapToSeeMarkers();

                mapView.invalidate();
            }
        });
    }


    private void updateStartingPoint(){
        View view = getFragmentView();
        TextView textView_puntoPartenza = view.findViewById(R.id.InsertItinerary_textView_nomePuntoPartenza);
        ImageView imageView_annullaPuntoPartenza = view.findViewById(R.id.InsertItinerary_imageView_iconClosePuntoPartenza);
        if (pianificaItinerarioModel.hasStartingPoint()){
            AddressModel startingPoint = pianificaItinerarioModel.getStartingPoint();
            textView_puntoPartenza.setText(startingPoint.getAddressName());
            imageView_annullaPuntoPartenza.setVisibility(View.VISIBLE);
            return;
        }
        textView_puntoPartenza.setText("");
        imageView_annullaPuntoPartenza.setVisibility(View.GONE);
    }

    private void updateDestinationPoint(){
        View view = getFragmentView();
        TextView textView_puntoDestinazione = view.findViewById(R.id.InsertItinerary_textView_nomePuntoDestinazione);
        ImageView imageView_annullaPuntoDestinazione = view.findViewById(R.id.InsertItinerary_imageView_iconClosePuntoDestinzione);
        if (pianificaItinerarioModel.hasDestinationPoint()){
            AddressModel destinationPoint = pianificaItinerarioModel.getDestinationPoint();
            textView_puntoDestinazione.setText(destinationPoint.getAddressName());
            imageView_annullaPuntoDestinazione.setVisibility(View.VISIBLE);
            return;
        }
        textView_puntoDestinazione.setText("");
        imageView_annullaPuntoDestinazione.setVisibility(View.GONE);
    }


    private void updateOptionsIntermediatePoints(){
        View view = getFragmentView();
        RelativeLayout relativeLayout_aggiungiPuntiIntermedi = view.findViewById(R.id.InsertItinerary_relativeLayout_aggiungiPuntoIntermedio);
        RelativeLayout relativeLayout_mostraPuntiIntermedi = view.findViewById(R.id.InsertItinerary_relativeLayout_mostraPuntiIntermedi);
        ConstraintLayout constraintLayout_puntiIntermedi = view.findViewById(R.id.InsertItinerary_constraintLayout_puntiIntermedi);

        if(!pianificaItinerarioModel.hasStartingPoint() || !pianificaItinerarioModel.hasDestinationPoint()){
            relativeLayout_aggiungiPuntiIntermedi.setVisibility(View.GONE);
            relativeLayout_mostraPuntiIntermedi.setVisibility(View.GONE);
            constraintLayout_puntiIntermedi.setVisibility(View.GONE);
            return;
        }

        relativeLayout_aggiungiPuntiIntermedi.setVisibility(View.VISIBLE);

        if(!pianificaItinerarioModel.hasIntermediatePoints()){
            relativeLayout_mostraPuntiIntermedi.setVisibility(View.GONE);
            constraintLayout_puntiIntermedi.setVisibility(View.GONE);
            return;
        }

        if(relativeLayout_mostraPuntiIntermedi.getVisibility() == View.GONE && constraintLayout_puntiIntermedi.getVisibility() == View.GONE){
            relativeLayout_mostraPuntiIntermedi.setVisibility(View.VISIBLE);
        }
    }


    private void updateOptionsSelectFromMap(){
        View view = getFragmentView();
        Integer indexPointSelectedOnList = pianificaItinerarioModel.getIndexPointSelected();
        AddressModel pointSelectedOnMap = pianificaItinerarioModel.getPointSelectedOnMap();


        ConstraintLayout constraintLayout_puntiInteresse = view.findViewById(R.id.InsertItinerary_constraintLayout_puntiInteresse);
        TextView textView_pointSelectedFromMap = view.findViewById(R.id.InsertItinerary_textView_selezionaPuntoInteressatoDaMappa);
        ConstraintLayout constraintLayout_optionsSelectFromMap = view.findViewById(R.id.InsertItinerary_constraintLayout_opzioniSelezioneDaMappa);
        TextView textView_namePointSelectedFromMap = view.findViewById(R.id.InsertItinerary_textView_nomeIndirizzoSelezionatoDaMappa);

        Button button_setAsStartingPoint = view.findViewById(R.id.InsertItinerary_button_impostaPuntoPartenza);
        Button button_setAsDestinationPoint = view.findViewById(R.id.InsertItinerary_button_impostaPuntoDestinazione);
        Button button_setAsIntermediatePoint = view.findViewById(R.id.InsertItinerary_button_impostaPuntoIntermedio);

        //NO RETURN FROM SEARCH POINT
        if(indexPointSelectedOnList == null){

            constraintLayout_puntiInteresse.setVisibility(View.VISIBLE);
            textView_pointSelectedFromMap.setVisibility(View.GONE);


            //NO ADDRESS SELECTED ON MAP
            if(pointSelectedOnMap == null){
                constraintLayout_optionsSelectFromMap.setVisibility(View.GONE);
                return;
            }

            //ADDRESS SELECTED ON MAP
            constraintLayout_optionsSelectFromMap.setVisibility(View.VISIBLE);
            textView_namePointSelectedFromMap.setText(pointSelectedOnMap.getAddressName());

            button_setAsStartingPoint.setVisibility(View.VISIBLE);
            button_setAsDestinationPoint.setVisibility(View.VISIBLE);
            if(pianificaItinerarioModel.hasStartingPoint() && pianificaItinerarioModel.hasDestinationPoint()) {
                button_setAsIntermediatePoint.setVisibility(View.VISIBLE);
            }
            else button_setAsIntermediatePoint.setVisibility(View.GONE);
            return;
        }

        //RETURN FROM SEARCH POINT
        constraintLayout_puntiInteresse.setVisibility(View.GONE);
        textView_pointSelectedFromMap.setVisibility(View.VISIBLE);

        //NO ADDRESS SELECTED ON MAP
        if(pointSelectedOnMap == null){
            constraintLayout_optionsSelectFromMap.setVisibility(View.GONE);
            return;
        }

        //ADDRESS SELECTED ON MAP
        constraintLayout_optionsSelectFromMap.setVisibility(View.VISIBLE);
        textView_namePointSelectedFromMap.setText(pointSelectedOnMap.getAddressName());

        Log.d("safdfdsfsdfds",": " + indexPointSelectedOnList);

        //IL PUNTO SELEZIONATO E' IL PUNTO INIZIALE
        if(indexPointSelectedOnList == PianificaItinerarioController.STARTING_POINT_CODE){
            Log.d("testGSDFDFSD", "STARTING POINT");
            button_setAsStartingPoint.setVisibility(View.VISIBLE);
            button_setAsDestinationPoint.setVisibility(View.GONE);
            button_setAsIntermediatePoint.setVisibility(View.GONE);
        }
        //IL PUNTO SELEZIONATO E' IL PUNTO DI DESTINAZIONE
        else if(indexPointSelectedOnList == PianificaItinerarioController.DESTINATION_POINT_CODE){
            Log.d("testGSDFDFSD", "DESTINATION POINT");
            button_setAsStartingPoint.setVisibility(View.GONE);
            button_setAsDestinationPoint.setVisibility(View.VISIBLE);
            button_setAsIntermediatePoint.setVisibility(View.GONE);
        }
        //IL PUNTO SELEZIONATO E' UN PUNTO INTERMEDIO (GIA' ESISTENTE OPPURE NUOVO)
        else if(pianificaItinerarioModel.isValidIndexPoint(indexPointSelectedOnList)){
            Log.d("testGSDFDFSD", "INTERMEDIATE POINT");
            button_setAsStartingPoint.setVisibility(View.GONE);
            button_setAsDestinationPoint.setVisibility(View.GONE);
            button_setAsIntermediatePoint.setVisibility(View.VISIBLE);
        }
        else{
            Log.d("testGSDFDFSD", "error");
            //todo errore
            return;
        }
    }


    private void updateOptionsSaveItinerary(){
        View view = getFragmentView();
        Button button_save = view.findViewById(R.id.InsertItinerary_button_salva);
        ConstraintLayout constraintLayout_durationLenght = view.findViewById(R.id.ListElementItinerary_constraintLayout_durationDistanceDifficulty);

        if(pianificaItinerarioModel.hasStartingPoint() && pianificaItinerarioModel.hasDestinationPoint()){
            button_save.setVisibility(View.VISIBLE);

            float duration = 0;
            float distance = 0;

            List<RouteLegModel> routeLegs = pianificaItinerarioModel.getRouteLegs();
            for(RouteLegModel routeLeg : routeLegs){
                duration = duration + routeLeg.getDuration();
                distance = distance + routeLeg.getDistance();
            }

            TextView textView_duration = view.findViewById(R.id.ListElementItinerary_textView_duration);
            TextView textView_distance = view.findViewById(R.id.ListElementItinerary_textView_distance);

            textView_duration.setText(TimeUtils.toDurationString(duration));

            textView_distance.setText(TimeUtils.toDistanceString(distance));

            constraintLayout_durationLenght.setVisibility(View.VISIBLE);
            return;
        }

        button_save.setVisibility(View.GONE);
        constraintLayout_durationLenght.setVisibility(View.GONE);
    }

    private void updateMapWithPointSelectedOnMapMarker(){
        AddressModel pointSelectedOnMap = pianificaItinerarioModel.getPointSelectedOnMap();

        if(pointSelectedOnMap == null) {
            selectionMarker.setVisible(false);
            return;
        }

        GeoPoint geoPoint = pointSelectedOnMap.getPoint();
        selectionMarker.setVisible(true);
        selectionMarker.setPosition(geoPoint);
        selectionMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER);
    }

    private void updateMapWithWaypointMarkers(){
        wayPointMarkers.getItems().clear();

        if(pianificaItinerarioModel.hasStartingPoint()){
            Marker markerStartingPoint = generateInterestPointMarker(PianificaItinerarioController.STARTING_POINT_CODE);
            wayPointMarkers.add(markerStartingPoint);
        }
        if(pianificaItinerarioModel.hasIntermediatePoints()){

            List<AddressModel> intermediatePoints = pianificaItinerarioModel.getIntermediatePoints();

            for(int i = 0; i < intermediatePoints.size(); i++){
                Marker markerIntermediatePoint = generateInterestPointMarker(i);
                wayPointMarkers.add(markerIntermediatePoint);
            }
        }
        if(pianificaItinerarioModel.hasDestinationPoint()){
            Marker markerDestinationPoint = generateInterestPointMarker(PianificaItinerarioController.DESTINATION_POINT_CODE);
            wayPointMarkers.add(markerDestinationPoint);
        }

    }

    private void updateMapWithRoutes(){

        List<Overlay> mapOverlays = mapView.getOverlays();
        if (routeTracks != null) {
            for (Overlay overlay : routeTracks) mapOverlays.remove(overlay);
            routeTracks = null;
        }

        List<RouteLegModel> route = pianificaItinerarioModel.getRouteLegs();
        if (route == null || route.isEmpty()) return;

        routeTracks = new ArrayList<Polyline>();
        for(RouteLegModel routeLeg: route){
            List<GeoPoint> geoPoints = routeLeg.getTrack();
            Polyline polyline = new Polyline();
            polyline.setPoints(geoPoints);
            routeTracks.add(polyline);
            mapOverlays.add(1, polyline);
        }

        for(int i = 0; i < routeTracks.size(); i++){
            Paint paint = routeTracks.get(i).getOutlinePaint();
            paint.setColor(0x800000FF);//blue
            paint.setStrokeWidth(10.0F);
        }
    }



    private void centerMapToSeeMarkers(){

        if(pianificaItinerarioModel.getPointSelectedOnMap() != null){
            return;
        }

        if(pianificaItinerarioModel.hasStartingPoint() && pianificaItinerarioModel.hasDestinationPoint()){
            BoundingBox boundingBox = generateBoundingBoxWithAllMarkers();

            Log.i(TAG, "north: " + boundingBox.getLatNorth() + "\nest: " + boundingBox.getLonEast() + "\nsud: " + boundingBox.getLatSouth() + "\nwest: " + boundingBox.getLonWest());

            mapView.zoomToBoundingBox(boundingBox, true);
        }

        else if (pianificaItinerarioModel.hasStartingPoint() || pianificaItinerarioModel.hasDestinationPoint()) {
            AddressModel address;

            if (pianificaItinerarioModel.hasStartingPoint()) address = pianificaItinerarioModel.getStartingPoint();
            else address = pianificaItinerarioModel.getDestinationPoint();

            GeoPoint geoPoint = address.getPoint();
            mapView.getController().zoomTo(15d, 1500l);
            mapView.getController().setCenter(geoPoint);
            mapView.getController().animateTo(geoPoint);
        }

    }


    private Marker generateInterestPointMarker(int index){
        Marker marker = new Marker(mapView);

        String stringIndex;
        AddressModel address;

        if(index == PianificaItinerarioController.STARTING_POINT_CODE){
            stringIndex = "S";
            address = pianificaItinerarioModel.getStartingPoint();
        }
        else if(index == PianificaItinerarioController.DESTINATION_POINT_CODE){
            stringIndex = "D";
            address = pianificaItinerarioModel.getDestinationPoint();
        }
        else{
            stringIndex = String.valueOf(index+1);
            address = pianificaItinerarioModel.getIntermediatePoint(index);
        }

        marker.setIcon(DrawableUtils.getBitmapWithText(getContext(), R.drawable.ic_waypoint,stringIndex));
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        marker.setVisible(true);
        marker.setPosition(address.getPoint());


        return marker;
    }

    public BoundingBox generateBoundingBoxWithAllMarkers(){
        List<Overlay> items = wayPointMarkers.getItems();
        List<GeoPoint> geoPoints = new ArrayList<GeoPoint>();

        if(items == null || items.isEmpty()){
            BoundingBox boundingBox = new BoundingBox();
            return boundingBox;
        }

        List<AddressModel> addresses = pianificaItinerarioModel.getInterestPoints();
        for(AddressModel address: addresses) geoPoints.add(address.getPoint());


        BoundingBox boundingBox = BoundingBox.fromGeoPoints(geoPoints);

        boundingBox.setLatNorth(boundingBox.getLatNorth() + 0.1F);
        boundingBox.setLatSouth(boundingBox.getLatSouth() - 0.1F);
        boundingBox.setLonEast(boundingBox.getLonEast() + 0.01F);
        boundingBox.setLonWest(boundingBox.getLonWest() - 0.01F);


        return boundingBox;
    }

}
