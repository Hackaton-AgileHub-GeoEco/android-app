package hackaton.agilehub.geoeco.app.fragments

import android.app.Fragment
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.view.View
import hackaton.agilehub.geoeco.app.R
import org.osmdroid.views.MapView
import hackaton.agilehub.geoeco.app.utils.KotterKnife
import org.osmdroid.util.GeoPoint
import android.util.TypedValue
import java.util.ArrayList
import org.osmdroid.views.overlay.OverlayItem
import android.view.MotionEvent
import android.view.GestureDetector
import android.widget.Toast

class GarbageMapFragment : Fragment() {

    private val osmMapView: MapView by KotterKnife.bindView(R.id.fragment_garbage_map_osm_map_view)

    private val osmPins = ArrayList<OverlayItem>()

    private val osmMapViewGestureListener = object : GestureDetector.SimpleOnGestureListener() {
        override fun onDown(motionEvent: MotionEvent): Boolean {
            return true
        }

        override fun onLongPress(motionEvent: MotionEvent) {
            val osmProjection = this@GarbageMapFragment
                .osmMapView
                .getProjection()

            val newLocation = osmProjection.fromPixels(motionEvent.getX().toInt(),
                                                       motionEvent.getY().toInt())

            Toast.makeText(this@GarbageMapFragment.getActivity(),
                           "Lat: ${newLocation.getLatitude()} and Long: ${newLocation.getLongitude()}",
                           Toast.LENGTH_SHORT).show()
        }
    }

    private val osmMapViewGestureDetector = GestureDetector(this.osmMapViewGestureListener)

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_garbage_map, container, false);
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super<Fragment>.onViewCreated(view, savedInstanceState)

        this.initializeOsmMapView()
        this.initializeOsmMapViewEventHandlers()
    }

    private fun initializeOsmMapViewEventHandlers() {
        this.osmMapView.setOnTouchListener {
            (source, motionEvent) ->
            this.onOsmMapView_Touch(source,
                                    motionEvent)
        }
    }

    private fun onOsmMapView_Touch(source: View, motionEvent: MotionEvent): Boolean {
        return this.osmMapViewGestureDetector.onTouchEvent(motionEvent)
    }

    private fun initializeOsmMapView() {
        var latitude = TypedValue()
        var longitude = TypedValue()

        val resources = this.getResources()
        resources.getValue(R.dimen.main_city_latitude, latitude, true)
        resources.getValue(R.dimen.main_city_longitude, longitude, true)

        this.osmMapView.setBuiltInZoomControls(true)
        this.osmMapView.setMultiTouchControls(true)

        val osmMapController = this.osmMapView.getController()
        osmMapController.setZoom(6)

        [suppress("CAST_NEVER_SUCCEEDS")]
        osmMapController.setCenter(GeoPoint(latitude.getFloat().toDouble(),
                                            longitude.getFloat().toDouble()))
    }
}