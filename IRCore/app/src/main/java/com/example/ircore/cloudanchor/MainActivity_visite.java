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

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ircore.R;

import java.util.List;

/**
 * Main Activity for the Cloud Anchors Codelab.
 *
 * <p>The bulk of the logic resides in the {@link CloudAnchorFragment}. The activity only creates
 * the fragment and attaches it to this Activity.
 */
public class MainActivity_visite extends AppCompatActivity {

  private TextView anchorsView;
  //private DataBaseManager dataBaseManager;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    Log.i("test","bonjour");
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main_visite);

    Intent intent =getIntent();
    if(intent!=null){


      //anchorsView = (TextView) findViewById(R.id.anchorsView);
      //dataBaseManager = new DataBaseManager(this);

    /*dataBaseManager.insertAnchor("1234ade",4);
    dataBaseManager.insertAnchor("567hyr",2);
    dataBaseManager.insertAnchor("347drgtg",1);
    dataBaseManager.insertAnchor("8536cx",2);*/
      //dataBaseManager.insertAnchor("nouvel anchor",2);//
      /*List<Ancrage> liste = dataBaseManager.readAnchors(2);
      for (Ancrage ancrage : liste){
        anchorsView.append(ancrage.toString());
      }*/

      //dataBaseManager.close();

      Log.i("DEBUG","On a passé la table");

    }




    //FragmentManager fm = getSupportFragmentManager();
    //Fragment frag = fm.findFragmentById(R.id.fragment);
    //if (frag == null) {
      //frag = new CloudAnchorFragment();
      //fm.beginTransaction().add(R.id.fragment, frag).commit();
    //}


  }

}
