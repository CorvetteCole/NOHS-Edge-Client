package corve.nohsedge;

import java.io.Serializable;

/**
 * Created by corve on 1/10/2018.
 */


class EdgeClass implements Serializable {

    private String mTitle;
    private String mTeacher;
    private int mDate;
    private String mTime;
    private String mDay;

    EdgeClass(String title, String teacher, int date, String day, String time){
        mTitle = title;
        mTeacher = teacher;
        mDate = date;
        mDay = day;
        mTime = time;
    }


   public String getTitle(){
        return mTitle;
    }

   public String getTeacher(){
        return mTeacher;
    }

   public int getDate(){
        return mDate;
    }

   public String getTime(){
        return mTime;
    }

   public String getDay(){
        return mDay;
    }


}
