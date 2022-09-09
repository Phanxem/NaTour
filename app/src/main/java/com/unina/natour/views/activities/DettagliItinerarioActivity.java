package com.unina.natour.views.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.unina.natour.R;
import com.unina.natour.controllers.DettagliItinerarioController;
import com.unina.natour.controllers.utils.DrawableUtils;
import com.unina.natour.models.AddressModel;
import com.unina.natour.models.DettagliItinerarioModel;
import com.unina.natour.views.dialogs.MessageDialog;

import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.FolderOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.Polyline;

import java.util.ArrayList;
import java.util.List;
@RequiresApi(api = Build.VERSION_CODES.N)
public class DettagliItinerarioActivity extends AppCompatActivity {

    private final static String TAG ="DettagliItinerarioActivity";

    private DettagliItinerarioController dettagliItinerarioController;

    private DettagliItinerarioModel dettagliItinerarioModel;

    MapView mapView;
    FolderOverlay wayPointMarkers;
    ArrayList<Polyline> routeTracks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dettagli_itinerario);

        MessageDialog messageDialog = new MessageDialog();
        messageDialog.setFragmentActivity(this);

        dettagliItinerarioController = new DettagliItinerarioController(this, messageDialog);
        dettagliItinerarioModel = dettagliItinerarioController.getModel();

        mapView = findViewById(R.id.ItineraryDetails_mapView_mappa);
        dettagliItinerarioController.initMap(mapView);

        List<Overlay> overlays = mapView.getOverlays();

        wayPointMarkers = new FolderOverlay();
        overlays.add(wayPointMarkers);

        routeTracks = null;



        dettagliItinerarioController.initOsmdroidConfiguration();

        update();
    }

    public void pressIconBack(){
        ImageView imageView_back = findViewById(R.id.ItineraryDetails_imageView_iconaIndietro);
        imageView_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO
            }
        });
    }

    public void pressIconMenu(){
        ImageView imageView_menu = findViewById(R.id.ItineraryDetails_imageView_iconaMenu);
        imageView_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO
            }
        });
    }

    public void pressWarning(){
        RelativeLayout relativeLayout_warning = findViewById(R.id.ItineraryDetails_relativeLayout_warning);
        relativeLayout_warning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO
            }
        });
    }

    public void pressButtonNavigation(){
        Button button_navigation = findViewById(R.id.ItineraryDetails_button_avviaNavigazione);
        button_navigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO
            }
        });
    }

    public void update(){

        RelativeLayout relativeLayout_warning = findViewById(R.id.ItineraryDetails_relativeLayout_warning);
        if(dettagliItinerarioModel.hasBeenReported()){
            relativeLayout_warning.setVisibility(View.VISIBLE);
        }
        else{
            relativeLayout_warning.setVisibility(View.GONE);
        }

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

        updateMapWithRoutes();
        updateMapWithWaypointMarkers();
        centerMapToSeeMarkers();

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