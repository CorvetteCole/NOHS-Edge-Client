package corve.nohsedge;

/**
 * Created by corve on 1/10/2018.
 */


class EdgeClass {

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


    String getTitle(){
        return mTitle;
    }

    String getTeacher(){
        return mTeacher;
    }

    int getDate(){
        return mDate;
    }

    String getTime(){
        return mTime;
    }

    String getDay(){
        return mDay;
    }


}
