package com.unina.natour.views.activities;

import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.unina.natour.R;
import com.unina.natour.config.CurrentUserInfo;
import com.unina.natour.controllers.DettagliItinerarioController;
import com.unina.natour.controllers.SegnalaItinerarioController;
import com.unina.natour.controllers.VisualizzaSegnalazioniController;
import com.unina.natour.controllers.utils.DrawableUtils;
import com.unina.natour.models.DettagliItinerarioModel;
import com.unina.natour.views.dialogs.EliminaItinerarioDialog;

import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.FolderOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.Polyline;

import java.util.ArrayList;
import java.util.List;



public class DettagliItinerarioActivity extends NaTourActivity {

    private DettagliItinerarioController dettagliItinerarioController;

    private DettagliItinerarioModel dettagliItinerarioModel;

    private MapView mapView;
    private FolderOverlay wayPointMarkers;
    private ArrayList<Polyline> routeTracks;
    private Marker markerCurrentPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dettagli_itinerario);

        Intent intent = this.getIntent();
        long idItinerary = intent.getLongExtra(DettagliItinerarioController.EXTRA_ITINERARY_ID,-1);

        dettagliItinerarioController = new DettagliItinerarioController(this, idItinerary);
        dettagliItinerarioModel = dettagliItinerarioController.getModel();
        dettagliItinerarioModel.registerObserver(this);
        addModel(dettagliItinerarioModel);

        mapView = findViewById(R.id.ItineraryDetails_mapView_mappa);
        dettagliItinerarioController.initMap(mapView);

        List<Overlay> overlays = mapView.getOverlays();

        wayPointMarkers = new FolderOverlay();
        overlays.add(wayPointMarkers);

        routeTracks = null;

        markerCurrentPosition = new Marker(mapView);
        markerCurrentPosition.setIcon(getResources().getDrawable(R.drawable.ic_your_position));
        markerCurrentPosition.setVisible(false);
        overlays.add(markerCurrentPosition);

        dettagliItinerarioController.initOsmdroidConfiguration();

        pressIconBack();
        pressButtonNavigation();
        pressButtonClose();
        pressIconMenu();
        pressWarning();
    }

    @Override
    protected void onResume() {
        Intent intent = this.getIntent();
        long idItinerary = intent.getLongExtra(DettagliItinerarioController.EXTRA_ITINERARY_ID,-1);

        dettagliItinerarioController.initModel(idItinerary);
        initUI();
        update();
        super.onResume();
    }

    public void pressIconBack(){
        ImageView imageView_back = findViewById(R.id.ItineraryDetails_imageView_iconaIndietro);
        imageView_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void pressIconMenu(){
        NaTourActivity activity = this;

        ImageView imageView_menu = findViewById(R.id.ItineraryDetails_imageView_iconaMenu);

        PopupMenu popupMenu = new PopupMenu(this,imageView_menu);

        if(dettagliItinerarioController.isMyItinerary()){
            popupMenu.getMenuInflater().inflate(R.menu.popup_menu_dettagli_itinerario_pesonale, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    if(item.getItemId() == R.id.DettagliItinerario_popupMenu_elimina){

                        EliminaItinerarioDialog eliminaItinerarioDialog = new EliminaItinerarioDialog(dettagliItinerarioModel.getItineraryId());
                        eliminaItinerarioDialog.setNaTourActivity(activity);
                        eliminaItinerarioDialog.showOverUi();
                        return true;
                    }
                    else return false;
                }
            });
        }
        else{
            popupMenu.getMenuInflater().inflate(R.menu.popup_menu_dettagli_itinerario_altri, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    if(item.getItemId() == R.id.DettagliItinerario_popupMenu_segnala){
                        SegnalaItinerarioController.openSegnalaItinerarioActivity(activity,dettagliItinerarioModel.getItineraryId());
                        return true;
                    }
                    else return false;
                }
            });
        }



        imageView_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupMenu.show();
            }
        });
    }

    public void pressWarning(){
        NaTourActivity activity = this;

        RelativeLayout relativeLayout_warning = findViewById(R.id.ItineraryDetails_relativeLayout_warning);
        relativeLayout_warning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VisualizzaSegnalazioniController.openVisualizzaSegnalazioniActivity(activity,dettagliItinerarioModel.getItineraryId());
            }
        });
    }

    public void pressButtonNavigation(){
        Button button_navigation = findViewById(R.id.ItineraryDetails_button_avviaNavigazione);
        button_navigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dettagliItinerarioController.activeNavigation();
                mapView.getController().zoomTo(15d, 1500l);
                mapView.getController().setCenter(dettagliItinerarioModel.getCurrentLocation());
                mapView.getController().animateTo(dettagliItinerarioModel.getCurrentLocation());

            }
        });
    }

    public void pressButtonClose(){
        Button button_close = findViewById(R.id.ItineraryDetails_button_close);
        button_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dettagliItinerarioController.deactivateNavigation();
            }
        });
    }


    @Override
    public void onBackPressed() {
        if(dettagliItinerarioModel.isNavigationActive()){
            dettagliItinerarioController.deactivateNavigation();
            return;
        }

        super.onBackPressed();
    }

    private void initUI(){
        TextView textView_name = findViewById(R.id.ItineraryDetails_textView_name);
        textView_name.setText(dettagliItinerarioModel.getName());

        TextView textView_duration = findViewById(R.id.ItineraryDetails_textView_duration);
        textView_duration.setText(dettagliItinerarioModel.getDuration());

        TextView textView_lenght = findViewById(R.id.ItineraryDetails_textView_distance);
        textView_lenght.setText(dettagliItinerarioModel.getLenght());

        TextView textView_difficulty = findViewById(R.id.ItineraryDetails_textView_difficulty);
        textView_difficulty.setText(dettagliItinerarioModel.getDifficulty());

        TextView textView_description = findViewById(R.id.ItineraryDetails_textView_descrizione);
        textView_description.setText(dettagliItinerarioModel.getDescription());


        if(dettagliItinerarioModel.getProfileImage() != null){
            ImageView imageView_profileImage = findViewById(R.id.ItineraryDetails_imageView_immagineProfilo);
            imageView_profileImage.setImageBitmap(dettagliItinerarioModel.getProfileImage());
        }


        TextView textView_username = findViewById(R.id.ItineraryDetails_textView_username);
        textView_username.setText(dettagliItinerarioModel.getUsername());

        updateMapWithRoutes();
        updateMapWithWaypointMarkers();
        centerMapToSeeMarkers();
    }

    public void update(){

        ConstraintLayout constraintLayout_durationDistanceDifficulty = findViewById(R.id.ItineraryDetails_constraintLayout_durationDistanceDifficulty);
        ScrollView scrollView_body = findViewById(R.id.ItineraryDetails_scrollView_body);
        ImageView imageView_back = findViewById(R.id.ItineraryDetails_imageView_iconaIndietro);
        ImageView imageView_menu = findViewById(R.id.ItineraryDetails_imageView_iconaMenu);

        Button button_close = findViewById(R.id.ItineraryDetails_button_close);

        RelativeLayout relativeLayout_warning = findViewById(R.id.ItineraryDetails_relativeLayout_warning);
        if(dettagliItinerarioModel.isHasBeenReported()){
            relativeLayout_warning.setVisibility(View.VISIBLE);
        }
        else{
            relativeLayout_warning.setVisibility(View.GONE);
        }

        Button button_navigation = findViewById(R.id.ItineraryDetails_button_avviaNavigazione);
        if(!CurrentUserInfo.isSignedIn()){
            button_navigation.setVisibility(View.GONE);
            imageView_menu.setVisibility(View.GONE);
        }
        else{
            if(dettagliItinerarioModel.isNavigationActive()){

                constraintLayout_durationDistanceDifficulty.setVisibility(View.GONE);
                scrollView_body.setVisibility(View.GONE);
                imageView_back.setVisibility(View.GONE);
                imageView_menu.setVisibility(View.GONE);
                relativeLayout_warning.setVisibility(View.GONE);

                button_close.setVisibility(View.VISIBLE);

                markerCurrentPosition.setVisible(true);
                markerCurrentPosition.setPosition(dettagliItinerarioModel.getCurrentLocation());
                markerCurrentPosition.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER);
            }
            else{
                markerCurrentPosition.setVisible(false);

                constraintLayout_durationDistanceDifficulty.setVisibility(View.VISIBLE);
                scrollView_body.setVisibility(View.VISIBLE);
                imageView_back.setVisibility(View.VISIBLE);
                imageView_menu.setVisibility(View.VISIBLE);

                if(dettagliItinerarioModel.hasBeenReported())relativeLayout_warning.setVisibility(View.VISIBLE);
                else relativeLayout_warning.setVisibility(View.GONE);

                button_close.setVisibility(View.GONE);
            }
        }



        mapView.invalidate();
    }


    private void updateMapWithWaypointMarkers(){

        List<GeoPoint> geoPoints = dettagliItinerarioModel.getWayPoints();

        Marker makerStartingPoint = generateInterestPointMarker("S",geoPoints.get(0));
        wayPointMarkers.add(makerStartingPoint);

        for(int i = 1; i < geoPoints.size() - 1; i++){
            Marker markerIntermediatePoint = generateInterestPointMarker(String.valueOf(i), geoPoints.get(i));
            wayPointMarkers.add(markerIntermediatePoint);
        }

        Marker makerDestinationPoint = generateInterestPointMarker("D",geoPoints.get(geoPoints.size()-1));
        wayPointMarkers.add(makerDestinationPoint);

    }

    private void updateMapWithRoutes(){
        List<Overlay> mapOverlays = mapView.getOverlays();
        List<GeoPoint> geoPoints = dettagliItinerarioModel.getRoutePoints();

        Polyline polyline = new Polyline();
        polyline.setPoints(geoPoints);
        routeTracks = new ArrayList<Polyline>();
        routeTracks.add(polyline);
        mapOverlays.add(0, polyline);

        Paint paint = polyline.getOutlinePaint();
        paint.setColor(0x800000FF);//blue
        paint.setStrokeWidth(10.0F);
    }


    private Marker generateInterestPointMarker(String label, GeoPoint geoPoint){
        Marker marker = new Marker(mapView);

        marker.setIcon(DrawableUtils.getBitmapWithText(this, R.drawable.ic_waypoint, label));
        marker.setVisible(true);
        marker.setPosition(geoPoint);
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER);

        return marker;
    }

    private void centerMapToSeeMarkers(){
        BoundingBox boundingBox = generateBoundingBoxWithAllMarkers();
        mapView.zoomToBoundingBox(boundingBox, true);
    }

    public BoundingBox generateBoundingBoxWithAllMarkers(){

        List<GeoPoint> geoPoints = dettagliItinerarioModel.getWayPoints();

        BoundingBox boundingBox = BoundingBox.fromGeoPoints(geoPoints);

        boundingBox.setLatNorth(boundingBox.getLatNorth() + 0.1F);
        boundingBox.setLatSouth(boundingBox.getLatSouth() - 0.1F);
        boundingBox.setLonEast(boundingBox.getLonEast() + 0.01F);
        boundingBox.setLonWest(boundingBox.getLonWest() - 0.01F);

        return boundingBox;
    }



}