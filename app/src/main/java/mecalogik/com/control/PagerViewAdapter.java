package mecalogik.com.control;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Ryzen on 11/04/2018.
 */

class PagerViewAdapter extends FragmentPagerAdapter{
    public PagerViewAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                ProfoleFragment proFileFragment = new ProfoleFragment();
                return proFileFragment;


            case 1:
                NotificacionFragment notificacionFragment = new NotificacionFragment();
                return notificacionFragment;
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return 2;
    }
}
