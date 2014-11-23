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
import android.content.Context
import org.osmdroid.views.overlay.Overlay
import android.graphics.Canvas
import org.osmdroid.DefaultResourceProxyImpl
import org.osmdroid.views.overlay.ItemizedIconOverlay

class GarbageMapFragment : Fragment() {

    private val osmMapView: MapView by KotterKnife.bindView(R.id.fragment_garbage_map_osm_map_view)

    private val osmPins = ArrayList<OverlayItem>()

    private val osmPinListener = object : ItemizedIconOverlay.OnItemGestureListener<OverlayItem> {
        override fun onItemSingleTapUp(index: Int, item: OverlayItem?): Boolean {
            val overlays = this@GarbageMapFragment.osmMapView
                .getOverlays()

            this@GarbageMapFragment.osmMapView
                .getController()
                .setCenter(item?.getPoint())

            return true
        }

        override fun onItemLongPress(index: Int, item: OverlayItem?): Boolean {
            // TODO: remove pin when long pressing on in
            return true
        }
    }

    private val osmMapViewGestureListener = object : GestureDetector.SimpleOnGestureListener() {
        override fun onDown(e: MotionEvent): Boolean {
            return true
        }

        override fun onLongPress(motionEvent: MotionEvent) {
            val osmProjection = this@GarbageMapFragment
                .osmMapView
                .getProjection()

            val longPressLocation = osmProjection.fromPixels(motionEvent.getX().toInt(),
                                                             motionEvent.getY().toInt())

            val resourceProxy = DefaultResourceProxyImpl(this@GarbageMapFragment.getActivity())
            val overlays = ArrayList<OverlayItem>()

            overlays.add(OverlayItem("New Overlay",
                                     "Overlay Description",
                                     longPressLocation as GeoPoint));

            this@GarbageMapFragment.osmMapView
                .getOverlays()
                .add(ItemizedIconOverlay(overlays,
                                         this@GarbageMapFragment.osmPinListener,
                                         resourceProxy))

            this@GarbageMapFragment.osmMapView
                .invalidate()
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
    }

    private fun initializeOsmMapView() {
        var latitude = TypedValue()
        var longitude = TypedValue()

        val resources = this.getResources()
        resources.getValue(R.dimen.main_city_latitude, latitude, true)
        resources.getValue(R.dimen.main_city_longitude, longitude, true)

        this.osmMapView.setBuiltInZoomControls(true)
        this.osmMapView.setMultiTouchControls(true)
        this.osmMapView.getOverlays().add(MapOverlay(this.getActivity()))

        val osmMapController = this.osmMapView.getController()
        osmMapController.setZoom(12)

        [suppress("CAST_NEVER_SUCCEEDS")]
        osmMapController.setCenter(GeoPoint(latitude.getFloat().toDouble(),
                                            longitude.getFloat().toDouble()))
    }

    inner class MapOverlay(context: Context) : Overlay(context) {
        override fun draw(c: Canvas?, osmv: MapView?, shadow: Boolean) {
            // Do Nothing
        }

        override fun onTouchEvent(event: MotionEvent?, mapView: MapView?): Boolean {
            this@GarbageMapFragment
                .osmMapViewGestureDetector
                .onTouchEvent(event)

            return false
        }
    }
}