package cn.yejh.mobilelearning.activity;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.yejh.mobilelearning.R;

public class WelcomeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);
        new Handler().postDelayed(() -> {
            startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
            finish();
        }, 2000);

        // if (savedInstanceState == null) {
        // getFragmentManager().beginTransaction()
        // .add(R.id.container, new PlaceholderFragment()).commit();
        // }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_login, container, false);
            return rootView;
        }
    }
}
