package com.bleros.zaltman.miniFlashLight;

import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.view.View;
import android.widget.ToggleButton;
import com.sony.smallapp.SmallAppWindow;
import com.sony.smallapp.SmallApplication;

public class MiniFlashLight extends SmallApplication
{
    private Camera mCamera = null;

    @Override
    public void onCreate()
    {
        super.onCreate();

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
    }

    @Override
    public void onStart()
    {
        super.onStart();
    }

    @Override
    public void onStop()
    {
        super.onStop();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        flashLightOff();
    }

    public void flashLightOn()
    {
        try
        {
            if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH))
            {
                mCamera = Camera.open();
                Camera.Parameters p = mCamera.getParameters();
                p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                mCamera.setParameters(p);
                mCamera.startPreview();
                mCamera.unlock();
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public void flashLightOff()
    {
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
}
