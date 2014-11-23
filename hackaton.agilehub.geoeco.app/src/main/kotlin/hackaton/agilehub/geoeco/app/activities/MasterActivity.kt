package hackaton.agilehub.geoeco.app.activities

import android.app.Activity
import android.os.Bundle
import hackaton.agilehub.geoeco.app.R
import hackaton.agilehub.geoeco.app.fragments.GarbageMapFragment

class MasterActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super<Activity>.onCreate(savedInstanceState)

        this.setContentView(R.layout.activity_master)
        this.getFragmentManager().beginTransaction()
            .add(R.id.activity_master_fragment_area, GarbageMapFragment())
            .commit()
    }
}