package want.com.authtest;

import android.content.Context;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import want.com.authtest.bean.Tag;

/**
 * Created by wisn on 2017/7/27.
 */

public class PermTagUtils {
    public static List<Tag> getPerm(Context context){
        List<Tag> data=new ArrayList<>();
        try {
            InputStreamReader isr = new InputStreamReader(context.getAssets().open("a.json"));
            JsonParser parser=new JsonParser();
            JsonObject object=(JsonObject)parser.parse(isr);
            JsonArray array=object.get("data").getAsJsonArray();
            Iterator<JsonElement> iterator = array.iterator();
            while(iterator.hasNext()){
                JsonElement next = iterator.next();
                if(next!=null){
                   data.add(new Tag(next.getAsString(),false));
                }
            }
        } catch (JsonIOException e) {
            e.printStackTrace();
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
        return data;
    }
    public static  List<Tag> getTag(){
        List<Tag> data=new ArrayList<>();
        data.add(new Tag("android",false));
        data.add(new Tag("windows",false));
        data.add(new Tag("device_management",false));
        data.add(new Tag("virtual_firealarm",false));
        data.add(new Tag("ios",false));
        data.add(new Tag("raspberrypi",false));
        data.add(new Tag("arduino",false));
        data.add(new Tag("android_sense",false));
        data.add(new Tag("scep_management",false));
        return data;
    }
}

/*
 private void initTagChroose() {
        final ArrayList<String> data_list = new ArrayList<String>();
        data_list.add("android");
        data_list.add("windows");
        data_list.add("device_management");
        data_list.add("virtual_firealarm");
        data_list.add("ios");
        data_list.add("raspberrypi");
        data_list.add("arduino");
        data_list.add("android_sense");
        data_list.add("scep_management");
        //适配器
        ArrayAdapter
                arr_adapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data_list);
        //设置样式
        arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //加载适配器
        TagChroose.setAdapter(arr_adapter);
        TagChroose.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                tag.setText(data_list.get(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
*/
