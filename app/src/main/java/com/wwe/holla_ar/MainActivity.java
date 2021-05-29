package com.wwe.holla_ar;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.Toast;

import com.google.ar.core.Anchor;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.BaseArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

public class MainActivity extends AppCompatActivity {

    ArFragment arFragment;
    private ModelRenderable bugattiRenderable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initializing ArFragment
        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.sceneformFragment);

        setupModel();

        /*
        add listener when the user tap on a plan
        lambda function to add object when the user tap on a plan
        lambda function created from BaseArFragment.OnTapArPlaneListener interface
         */
        arFragment.setOnTapArPlaneListener((HitResult hitResult, Plane plane, MotionEvent motionEvent) -> {
            // When the user tapes on plan , we will add the model
            Anchor anchor= hitResult.createAnchor();
            AnchorNode anchorNode = new AnchorNode(anchor);
            anchorNode.setParent(arFragment.getArSceneView().getScene());

            createModel(anchorNode);

        });

    }

    /**
     * setupModel method create renderable models, then store them in a variable.
     */
    private void setupModel() {
        // call the ModelRenderable class to create renderable object.
        ModelRenderable.builder()
                .setSource(this, R.raw.mercedes_benz_gls)
                // call the lambda function in thenAccept if the model successfully loaded
                .build().thenAccept(renderable -> bugattiRenderable = renderable)
                // call the lambda function in exceptionally if the model fail to render
                .exceptionally(throwable -> {
                            Toast.makeText(this, "Unable to load bear model", Toast.LENGTH_SHORT).show();
                            return null;
                        }
                );
    }
    /**
     * method to add renderable object to the environment.
     * @param anchorNode node in which the object will be rendered
     */
    private void createModel(AnchorNode anchorNode) {
        TransformableNode node = new TransformableNode(arFragment.getTransformationSystem());
        node.setParent(anchorNode);
        node.setRenderable(bugattiRenderable);
        node.select();
    }
}
