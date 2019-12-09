package com.levent.pia;

import java.util.ArrayList;

public class kilolukurun {

    int imageId;
    static ArrayList<kilolukurun> dataList = new ArrayList<kilolukurun>();

    public kilolukurun() {

    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public static ArrayList setData()
    {
      int[] resimler = { R.drawable.cigmanti, R.drawable.zyagliyapraksarma, R.drawable.etliyapraksarma,
              R.drawable.etlilahanasarma, R.drawable.suboregi, R.drawable.kolboregi, R.drawable.mercimekkoftesi,
              R.drawable.havuctarator, R.drawable.burmabaklava, R.drawable.evbaklavasi
      };

      for(int i=0; i<resimler.length; i++)
      {
          kilolukurun urun = new kilolukurun();
          urun.setImageId(resimler[i]);
          dataList.add(urun);

      }

      return dataList;
    }
}
