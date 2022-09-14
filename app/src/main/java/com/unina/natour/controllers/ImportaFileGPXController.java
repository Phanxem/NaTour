package com.unina.natour.controllers;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.widget.ListView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.unina.natour.controllers.exceptionHandler.ExceptionHandler;
import com.unina.natour.controllers.exceptionHandler.exceptions.ServerException;
import com.unina.natour.controllers.exceptionHandler.exceptions.subAppException.FailureFindAddressException;
import com.unina.natour.controllers.exceptionHandler.exceptions.subAppException.FailureReadGPXFileException;
import com.unina.natour.controllers.exceptionHandler.exceptions.subAppException.InvalidURLFormatException;
import com.unina.natour.controllers.exceptionHandler.exceptions.subAppException.NotCompletedFindAddressException;
import com.unina.natour.controllers.exceptionHandler.exceptions.subAppException.NotExistDirectoryException;
import com.unina.natour.controllers.exceptionHandler.exceptions.subAppException.NotExistParentDirectoryException;
import com.unina.natour.controllers.utils.FileUtils;
import com.unina.natour.models.AddressModel;
import com.unina.natour.models.ImportaFileGPXModel;
import com.unina.natour.models.RouteLegModel;
import com.unina.natour.models.dao.implementation.AddressDAOImpl;
import com.unina.natour.models.dao.interfaces.AddressDAO;
import com.unina.natour.views.activities.ImportaFileGPXActivity;
import com.unina.natour.views.activities.NaTourActivity;
import com.unina.natour.views.dialogs.MessageDialog;
import com.unina.natour.views.listAdapters.FileGpxListAdapter;

import org.osmdroid.util.GeoPoint;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

import io.jenetics.jpx.GPX;
import io.jenetics.jpx.Latitude;
import io.jenetics.jpx.Longitude;
import io.jenetics.jpx.Track;
import io.jenetics.jpx.TrackSegment;
import io.jenetics.jpx.WayPoint;

@RequiresApi(api = Build.VERSION_CODES.N)
public class ImportaFileGPXController extends NaTourController{

    public static final int REQUEST_CODE = 98;
    public static final int RESULT_CODE_RETURN_ALL_ADDRESSES = 0;
    public static final String EXTRA_ADDRESSES = "Addresses";


    private FileGpxListAdapter fileGpxListAdapter;

    private ImportaFileGPXModel importaFileGPXModel;

    private AddressDAO addressDAO;


    public ImportaFileGPXController(NaTourActivity activity){
        super(activity);

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
        NotExistParentDirectoryException exception = new NotExistParentDirectoryException();
        ExceptionHandler.handleMessageError(getMessageDialog(),exception);
    }

    public void openDirectory(File directory) {
        if(directory == null || !directory.isDirectory()){
            NotExistDirectoryException exception = new NotExistDirectoryException();
            ExceptionHandler.handleMessageError(getMessageDialog(),exception);
            return;
        }

        List<File> files = Arrays.asList(directory.listFiles(FileUtils.extensionFileFilter(FileUtils.EXTENSION_GPX)));
        importaFileGPXModel.set(directory,files);
        fileGpxListAdapter.notifyDataSetChanged();
    }

    public boolean readGPXFile(File gpxFile) {
        GPX.Reader gpxReader = GPX.reader();
        GPX gpx = null;

        try {
            gpx = gpxReader.read(gpxFile);
        }
        catch (IOException e) {
            FailureReadGPXFileException exception = new FailureReadGPXFileException();
            ExceptionHandler.handleMessageError(getMessageDialog(),exception);
            return false;
        }
        if(gpx == null){
            FailureReadGPXFileException exception = new FailureReadGPXFileException();
            ExceptionHandler.handleMessageError(getMessageDialog(),exception);
            return false;
        }


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

            TrackSegment firstSegment = trackSegments.get(0);
            TrackSegment lastSegment = trackSegments.get(trackSegments.size()-1);

            List<WayPoint> firstSegmentWayPoints = firstSegment.getPoints();
            List<WayPoint> lastSegmentWayPoints = lastSegment.getPoints();

            firstWayPoint = firstSegmentWayPoints.get(0);
            lastWayPoint = lastSegmentWayPoints.get(lastSegmentWayPoints.size()-1);

            Latitude lat = firstWayPoint.getLatitude();
            Longitude lon = firstWayPoint.getLongitude();
            GeoPoint startGeoPoint = new GeoPoint(lat.doubleValue(),lon.doubleValue());

            lat = lastWayPoint.getLatitude();
            lon = lastWayPoint.getLongitude();
            GeoPoint destinationGeoPoint = new GeoPoint(lat.doubleValue(), lon.doubleValue());

            geoPoints.add(startGeoPoint);
            geoPoints.add(destinationGeoPoint);
        }
        else {
            FailureReadGPXFileException exception = new FailureReadGPXFileException();
            ExceptionHandler.handleMessageError(getMessageDialog(),exception);
            return false;
        }


        ArrayList<AddressModel> addresses = new ArrayList<AddressModel>();
        for(GeoPoint geoPoint: geoPoints){
            AddressModel address = null;
            try {
                address = addressDAO.findAddressByGeoPoint(geoPoint);
            }
            catch (ServerException e) {
                ExceptionHandler.handleMessageError(getMessageDialog(),e);
            }
            catch (IOException e) {
                if(e instanceof UnsupportedEncodingException){
                    InvalidURLFormatException exception = new InvalidURLFormatException(e);
                    ExceptionHandler.handleMessageError(getMessageDialog(),exception);
                    return false;
                }
                FailureFindAddressException exception = new FailureFindAddressException(e);
                ExceptionHandler.handleMessageError(getMessageDialog(),exception);
                return false;

            }
            catch (ExecutionException | InterruptedException e) {
                NotCompletedFindAddressException exception = new NotCompletedFindAddressException(e);
                ExceptionHandler.handleMessageError(getMessageDialog(),exception);
                return false;
            }

            addresses.add(address);
        }

        Intent intent = new Intent();
        intent.putParcelableArrayListExtra(EXTRA_ADDRESSES, addresses);
        getActivity().setResult(RESULT_CODE_RETURN_ALL_ADDRESSES, intent);
        getActivity().finish();
        return true;

    }

    public ImportaFileGPXModel getImportaFileGPXModel(){
        return this.importaFileGPXModel;
    }

    public static void openImportaFileGPXActivity(NaTourActivity fromActivity, ActivityResultLauncher<Intent> activityResultLauncherImportaFileGPX, ActivityResultLauncher<String> activityResultLauncherPermissions){
        if (ActivityCompat.checkSelfPermission(fromActivity, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            activityResultLauncherPermissions.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
            return;
        }

        Intent intent = new Intent(fromActivity, ImportaFileGPXActivity.class);
        activityResultLauncherImportaFileGPX.launch(intent);
    }
}
