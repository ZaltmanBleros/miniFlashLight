package com.bleros.zaltman.miniFlashLight;

import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.util.Log;
import android.view.View;
import android.widget.ToggleButton;
import com.sony.smallapp.SmallAppWindow;
import com.sony.smallapp.SmallApplication;

public class MiniFlashLight extends SmallApplication
{
    private static final String TAG = MiniFlashLight.class.getSimpleName();
    private Camera mCamera;
    private Configuration mConfig;

    @Override
    public void onCreate()
    {
        Log.d(TAG, "onCreate()");
        super.onCreate();

        mConfig = new Configuration(getResources().getConfiguration());
        setContentView(R.layout.main);
        setTitle(R.string.app_name);

        SmallAppWindow.Attributes attr = getWindow().getAttributes();
        attr.flags &= ~SmallAppWindow.Attributes.FLAG_RESIZABLE;
        attr.flags |= SmallAppWindow.Attributes.FLAG_NO_TITLEBAR;
        getWindow().setAttributes(attr);

        final ToggleButton toggleButton = (ToggleButton) findViewById(R.id.toggleButton);
        toggleButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (toggleButton.isChecked())
                {
                    flashLightOn();
                }
                else
                {
                    flashLightOff();
                }
            }
        });

        toggleButton.setChecked(flashLightOn());
    }

    @Override
    public void onStart()
    {
        Log.d(TAG, "onStart()");
        super.onStart();
    }

    @Override
    public void onStop()
    {
        Log.d(TAG, "onStop()");
        super.onStop();
    }

    @Override
    public void onDestroy()
    {
        Log.d(TAG, "onDestroy()");
        super.onDestroy();
        flashLightOff();
    }

    public boolean flashLightOn()
    {
        Log.d(TAG, "flashLightOn()");
        try
        {
            if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH))
            {
                mCamera = Camera.open();
                Camera.Parameters p = mCamera.getParameters();
                p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                mCamera.setParameters(p);
                mCamera.startPreview();
                return true;
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        return false;
    }

    public void flashLightOff()
    {
        Log.d(TAG, "flashLightOff()");
        try
        {
            if ((mCamera != null) && getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH))
            {
                mCamera.stopPreview();
                mCamera.release();
                mCamera = null;
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    protected boolean onSmallAppConfigurationChanged(Configuration newConfig)
    {
        Log.d(TAG, "onSmallAppConfigurationChanged()");
        int diff = newConfig.diff(mConfig);
        mConfig = new Configuration(getResources().getConfiguration());

        // Avoid application from restarting when orientation changed
        if ((diff & ActivityInfo.CONFIG_ORIENTATION) != 0)
        {
            Log.d(TAG, "onSmallAppConfigurationChanged() - change of orientation detected...");
            return true;
        }

        return super.onSmallAppConfigurationChanged(newConfig);
    }
}
