/*
 * Copyright 2019 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.ircore.cloudanchor;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.ircore.R;
import com.example.ircore.cloudanchor.helpers.CloudAnchorManager;
import com.example.ircore.cloudanchor.helpers.SnackbarHelper;
import com.google.ar.core.Anchor;
import com.google.ar.core.Anchor.CloudAnchorState;
import com.google.ar.core.Config;
import com.google.ar.core.Config.CloudAnchorMode;
import com.google.ar.core.HitResult;
import com.google.ar.core.Session;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.Scene;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.Renderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

import java.util.ArrayList;
import java.util.List;

/**
 * Main Fragment for the Cloud Anchors Codelab.
 *
 * <p>This is where the AR Session and the Cloud Anchors are managed.
 */
public class CloudAnchorFragment extends ArFragment {

  private Scene arScene;
  private AnchorNode anchorNode;
  private ModelRenderable andyRenderable;
  private Button resolveButton;

  private DataBaseManager dataBaseManager;
  private ArrayList<String> people;

  private final CloudAnchorManager cloudAnchorManager = new CloudAnchorManager();
  private final SnackbarHelper snackbarHelper = new SnackbarHelper();

  private int place;

  @Override
  @SuppressWarnings({"AndroidApiChecker", "FutureReturnValueIgnored"})
  public void onAttach(Context context) {
    super.onAttach(context);
    ModelRenderable.builder()
        .setSource(context, R.raw.andy)
        .build()
        .thenAccept(renderable -> andyRenderable = renderable);
  }

  @Override
  public View onCreateView(
          LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    // Inflate from the Layout XML file.
    View rootView = inflater.inflate(R.layout.cloud_anchor_fragment, container, false);
    ConstraintLayout arContainer = rootView.findViewById(R.id.ar_container);

    // Call the ArFragment's implementation to get the AR View.
    View arView = super.onCreateView(inflater, arContainer, savedInstanceState);
    arContainer.addView(arView);

    Button clearButton = rootView.findViewById(R.id.clear_button);
    clearButton.setOnClickListener(v -> onClearButtonPressed());

    resolveButton = rootView.findViewById(R.id.resolve_button);
    resolveButton.setOnClickListener(v -> onResolveButtonPressed());

    initializeGallery(rootView);

    people=new ArrayList<String>();
    dataBaseManager = new DataBaseManager(this.getContext());

    arScene = getArSceneView().getScene();
    arScene.addOnUpdateListener(frameTime -> cloudAnchorManager.onUpdate());
    setOnTapArPlaneListener((hitResult, plane, motionEvent) -> onArPlaneTap(hitResult));
    return rootView;
  }




  //code pour ajouter les anchors

  private synchronized void onResolveButtonPressed() {
    //ResolveDialogFragment dialog =
        //ResolveDialogFragment.createWithOkListener(this::onShortCodeEntered);
    //dialog.show(getFragmentManager(), "Resolve");
    //onShortCodeEntered();
    List<Ancrage> liste = dataBaseManager.readAnchors(2);
    for (Ancrage ancrage : liste){
      String cloudAnchorId = ancrage.getIdAnchor();
      if (cloudAnchorId == null || cloudAnchorId.isEmpty()) {
        snackbarHelper.showMessage(
                getActivity(),
                "Pas de ID dans la liste");
        return;
      }
      //resolveButton.setEnabled(false);
      cloudAnchorManager.resolveCloudAnchor(
              getArSceneView().getSession(),
              cloudAnchorId,
              anchor -> onResolvedAnchorAvailable(anchor));
    }
    //for (String cloudAnchorId:people
    //) {

    //}
    //String cloudAnchorId = people.get(0);
  }

  //private synchronized void onShortCodeEntered() { //ecrire n'importe quoi pur le moment
    //String cloudAnchorId = storageManager.getCloudAnchorId(getActivity(), shortCode);


  

  private synchronized void onResolvedAnchorAvailable(com.google.ar.core.Anchor anchor) {
    CloudAnchorState cloudState = anchor.getCloudAnchorState();
    if (cloudState == CloudAnchorState.SUCCESS) {
      snackbarHelper.showMessage(getActivity(), "Cloud Ancrage Resolved. Son ID était :"+people.get(0) );
      setNewAnchor(anchor);
    }



    else {
      snackbarHelper.showMessage(
          getActivity(),
          "Le anchor associé à l'ID de la liste n'existe pas "

              + ". Error: "
              + cloudState.toString());
      resolveButton.setEnabled(true);
    }
  }




  //Tout le code d'ajout d'un anchor :

  private synchronized void onArPlaneTap(HitResult hitResult) {
    Anchor anchor = hitResult.createAnchor();
    setNewAnchor(anchor);
    snackbarHelper.showMessage(getActivity(), "Now hosting anchor...");
    cloudAnchorManager.hostCloudAnchor(
        getArSceneView().getSession(), anchor, this::onHostedAnchorAvailable);
  }

  private synchronized void onHostedAnchorAvailable(Anchor anchor) {
    CloudAnchorState cloudState = anchor.getCloudAnchorState();
    if (cloudState == CloudAnchorState.SUCCESS) {
      people.add(anchor.getCloudAnchorId());
      dataBaseManager.insertAnchor(anchor.getCloudAnchorId(),2);

      snackbarHelper.showMessage(getActivity(), "New Ancrage Hosted");
      setNewAnchor(anchor);
    } else {
      snackbarHelper.showMessage(getActivity(), "Error while hosting: " + cloudState.toString());
    }
  }

  private synchronized void onClearButtonPressed() {
    // Clear the anchor from the scene.
    cloudAnchorManager.clearListeners();
    resolveButton.setEnabled(true);

    List<Node> children = new ArrayList<Node>(getArSceneView().getScene().getChildren());
    for (Node node : children) {
      if (node instanceof AnchorNode) {
        if (((AnchorNode) node).getAnchor() != null) {
          ((AnchorNode) node).getAnchor().detach();
        }
      }
    }

    }



    //setNewAnchor(null);


  // Modify the renderables when a new ancrage is available.
  private synchronized void setNewAnchor(@Nullable Anchor anchor) {
    //if (anchorNode != null) {
      //arScene.removeChild(anchorNode);
      //anchorNode = null;
    //}
    if (anchor != null) {
      if (andyRenderable == null) {
        Toast toast = Toast.makeText(getContext(), "Andy model was not loaded.", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
        return;
      }
      // Create the Ancrage.
      anchorNode = new AnchorNode(anchor);
      arScene.addChild(anchorNode);

      // Create the transformable andy and add it to the ancrage.
      TransformableNode andy = new TransformableNode(getTransformationSystem());
      andy.setParent(anchorNode);
      andy.setRenderable(andyRenderable);
      andy.select();
    }
  }

  @Override
  protected Config getSessionConfiguration(Session session) {
    Config config = super.getSessionConfiguration(session);
    config.setCloudAnchorMode(CloudAnchorMode.ENABLED);
    return config;
  }

  private void initializeGallery(View rootview) {
    ConstraintLayout gallery = rootview.findViewById(R.id.gallery_layout);

    ImageView andy = new ImageView(this.getContext());
    andy.setImageResource(R.drawable.droid_thumb);
    andy.setContentDescription("andy");
    andy.setOnClickListener(view -> {
      ModelRenderable.builder()
              .setSource(this.getContext(), Uri.parse("andy_dance.sfb"))
              .build()
              .thenAccept(renderable -> andyRenderable = renderable);
      Log.i("test","andy");

    });
    gallery.addView(andy);

    ImageView cabin = new ImageView(this.getContext());
    cabin.setImageResource(R.drawable.cabin_thumb);
    cabin.setContentDescription("cabin");
    cabin.setOnClickListener(view -> {
      ModelRenderable.builder()
              .setSource(this.getContext(), Uri.parse("Cabin.sfb"))
              .build()
              .thenAccept(renderable -> andyRenderable = renderable);
      Log.i("test","maison");

    });
    gallery.addView(cabin);

  }
}
