package autonsi.smasttvapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class USBCameraActivity extends AppCompatActivity {
    CameraPreview cp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usb_camera);
        cp = (CameraPreview) findViewById(R.id.cp);
    }
}