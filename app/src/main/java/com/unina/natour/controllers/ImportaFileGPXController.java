package com.unina.natour.controllers;

import android.app.Activity;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;
import android.widget.ListView;

import com.unina.natour.controllers.exceptionHandler.exceptions.ServerException;
import com.unina.natour.controllers.utils.FileUtils;
import com.unina.natour.models.AddressModel;
import com.unina.natour.models.ImportaFileGPXModel;
import com.unina.natour.models.RouteLegModel;
import com.unina.natour.models.dao.implementation.AddressDAOImpl;
import com.unina.natour.models.dao.interfaces.AddressDAO;
import com.unina.natour.views.dialogs.MessageDialog;
import com.unina.natour.views.listAdapters.FileGpxListAdapter;

import org.osmdroid.util.GeoPoint;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.jenetics.jpx.GPX;
import io.jenetics.jpx.Latitude;
import io.jenetics.jpx.Longitude;
import io.jenetics.jpx.Track;
import io.jenetics.jpx.TrackSegment;
import io.jenetics.jpx.WayPoint;

public class ImportaFileGPXController {
    public static final int REQUEST_CODE = 98;

    public static final int RESULT_CODE_RETURN_ALL_ADDRESSES = 0;

    public static final String EXTRA_ADDRESSES = "Addresses";

    Activity activity;
    MessageDialog messageDialog;

    private FileGpxListAdapter fileGpxListAdapter;

    private ImportaFileGPXModel importaFileGPXModel;

    private AddressDAO addressDAO;

    public ImportaFileGPXController(Activity activity){
        this.activity = activity;
        this.messageDialog = new MessageDialog(activity);

        this.importaFileGPXModel = new ImportaFileGPXModel();

        this.fileGpxListAdapter = new FileGpxListAdapter(
                activity,
                importaFileGPXModel.getFiles(),
                this
        );
        this.addressDAO = new AddressDAOImpl();

    }

    public void initListViewFiles(ListView listView_files) {
        listView_files.setAdapter(fileGpxListAdapter);
    }

    public void openRootDirectory(){
        //importaFileGPXController.openDirectory(getExternalFilesDir());
        openDirectory(Environment.getExternalStorageDirectory());
        //importaFileGPXController.openDirectory(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS));

    }

    public void openParentDirectory(){
        if(importaFileGPXModel.hasParentDirectory()){
            openDirectory(importaFileGPXModel.getParentDirectory());
            return;
        }
        //TODO ERRORE
    }

    public void openDirectory(File directory) {
        if(directory == null || !directory.isDirectory()){
            return;
        }

        List<File> files = Arrays.asList(directory.listFiles(FileUtils.extensionFileFilter(FileUtils.EXTENSION_GPX)));
        //List<File> files = Arrays.asList(directory.listFiles());

        importaFileGPXModel.set(directory,files);
        fileGpxListAdapter.notifyDataSetChanged();

        return;

    }

    public boolean readGPXFile(File gpxFile) {
        GPX.Reader gpxReader = GPX.reader();

        try {
            Log.i("START", "starting");
            GPX gpx = gpxReader.read(gpxFile);
            if(gpx == null) return false;

            List<GeoPoint> geoPoints = new ArrayList<GeoPoint>();

            List<WayPoint> wayPoints = gpx.getWayPoints();
            List<Track> tracks = gpx.getTracks();
            if(wayPoints != null && !wayPoints.isEmpty()){
                for(WayPoint wayPoint : wayPoints){
                    GeoPoint geoPoint = new GeoPoint(wayPoint.getLatitude().doubleValue(),wayPoint.getLongitude().doubleValue());
                    geoPoints.add(geoPoint);
                }
            }
            else if(tracks != null && !tracks.isEmpty() ){
                Track track = tracks.get(0);

                List<TrackSegment> trackSegments = track.getSegments();

                if(trackSegments == null || trackSegments.isEmpty()) return false;

                WayPoint firstWayPoint;
                WayPoint lastWayPoint;

                RouteLegModel routeLegModel = new RouteLegModel();

                if(trackSegments.size() == 1){
                    TrackSegment segment = trackSegments.get(0);

                    List<WayPoint> segmentWayPoints = segment.getPoints();

                    firstWayPoint = segmentWayPoints.get(0);
                    lastWayPoint = segmentWayPoints.get(segmentWayPoints.size()-1);
                }
                else{
                    TrackSegment firstSegment = trackSegments.get(0);
                    TrackSegment lastSegment = trackSegments.get(trackSegments.size()-1);

                    List<WayPoint> firstSegmentWayPoints = firstSegment.getPoints();
                    List<WayPoint> lastSegmentWayPoints = lastSegment.getPoints();

                    firstWayPoint = firstSegmentWayPoints.get(0);
                    lastWayPoint = lastSegmentWayPoints.get(lastSegmentWayPoints.size()-1);
                }

                Latitude lat = firstWayPoint.getLatitude();
                Longitude lon = firstWayPoint.getLongitude();
                GeoPoint startGeoPoint = new GeoPoint(lat.doubleValue(),lon.doubleValue());

                lat = lastWayPoint.getLatitude();
                lon = lastWayPoint.getLongitude();
                GeoPoint destinationGeoPoint = new GeoPoint(lat.doubleValue(), lon.doubleValue());

                geoPoints.add(startGeoPoint);
                geoPoints.add(destinationGeoPoint);
            }
            else{
                return false;
            }

            ArrayList<AddressModel> addresses = new ArrayList<AddressModel>();
            for(GeoPoint geoPoint: geoPoints){
                AddressModel address = addressDAO.findAddressByGeoPoint(geoPoint);
                addresses.add(address);
            }

            Log.i("END", "ending");

            Intent intent = new Intent();
            intent.putParcelableArrayListExtra(EXTRA_ADDRESSES, addresses);
            activity.setResult(RESULT_CODE_RETURN_ALL_ADDRESSES, intent);
            activity.finish();
            return true;

        }
        catch (IOException e) {
            Log.e("GPXCONTROLLER", "errore import", e);
        }
        catch (UnknownException e) {
            e.printStackTrace();
        }
        catch (ServerException e) {
            e.printStackTrace();
        }

        return false;
    }

    public ImportaFileGPXModel getImportaFileGPXModel(){
        return this.importaFileGPXModel;
    }


/*
            if(!hasWayPoints){

                Length length = gpx.tracks()
                        .flatMap(Track::segments)
                        .findFirst()
                        .map(TrackSegment::points).orElse(Stream.empty())
                        .collect(Geoid.WGS84.toPathLength());




                Track track = tracks.get(0);

                List<TrackSegment> trackSegments = track.getSegments();


                if(trackSegments == null || trackSegments.isEmpty()) return false;

                WayPoint firstWayPoint;
                WayPoint lastWayPoint;

                RouteLegModel routeLegModel = new RouteLegModel();

                for(TrackSegment segment: trackSegments){
                    List<WayPoint>  segmentWayPoints = segment.getPoints();

                    for(WayPoint wayPoint: segmentWayPoints){

                    }


                }




                if(trackSegments.size() == 1){
                    TrackSegment segment = trackSegments.get(0);

                    List<WayPoint> segmentWayPoints = segment.getPoints();

                    firstWayPoint = segmentWayPoints.get(0);
                    lastWayPoint = segmentWayPoints.get(segmentWayPoints.size()-1);
                }
                else{
                    TrackSegment firstSegment = trackSegments.get(0);
                    TrackSegment lastSegment = trackSegments.get(trackSegments.size()-1);

                    List<WayPoint> firstSegmentWayPoints = firstSegment.getPoints();
                    List<WayPoint> lastSegmentWayPoints = lastSegment.getPoints();

                    firstWayPoint = firstSegmentWayPoints.get(0);
                    lastWayPoint = lastSegmentWayPoints.get(lastSegmentWayPoints.size()-1);
                }

                Latitude lat = firstWayPoint.getLatitude();
                Longitude lon = firstWayPoint.getLongitude();
                GeoPoint startGeoPoint = new GeoPoint(lat.doubleValue(),lon.doubleValue());

                lat = lastWayPoint.getLatitude();
                lon = lastWayPoint.getLongitude();
                GeoPoint destinationGeoPoint = new GeoPoint(lat.doubleValue(), lon.doubleValue());


            }
*/


}
