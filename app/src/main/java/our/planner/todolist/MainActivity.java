package our.planner.todolist;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    final List<String> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        final ListView ListView = findViewById(R.id.ListView);
        final TextAdapter adapter = new TextAdapter();

        readAll();

        adapter.setdata(list);
        ListView.setAdapter(adapter);


        ListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Remove task?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                list.remove(position);
                                adapter.setdata(list);
                            }
                        })
                        .setNegativeButton("No", null)
                        .create();
                dialog.show();
                            }


        });






        final Button newtaskbutton = findViewById(R.id.newtaskbutton);

        newtaskbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText taskinput = new EditText(MainActivity.this);
                taskinput.setSingleLine();
                AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Add new task")
                        .setView(taskinput)
                        .setPositiveButton("add task", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                list.add(taskinput.getText().toString());
                                adapter.setdata(list);
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create();
                dialog.show();
            }
        });

    }

    @Override
    protected void onPause(){
        super.onPause();
        saveAll();
    }

    private void saveAll(){
        try {
            File file = new File(this.getFilesDir(), "saved");

            FileOutputStream fout = new FileOutputStream(file);
            BufferedWriter br = new BufferedWriter(new OutputStreamWriter(fout));

            for (int i = 0; i<list.size(); i++){
                br.write(list.get(i));
                br.newLine();
            }
            br.close();
            fout.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void readAll(){
        File file = new File(this.getFilesDir(), "saved");
        if (!file.exists()){
            return;
        }

        try {
            FileInputStream is = new FileInputStream(file);
            BufferedReader bw = new BufferedReader(new InputStreamReader(is));
            String line = bw.readLine();
            while (line!=null){
                list.add(line);
                line = bw.readLine();
            }
        }catch (Exception e){
            e.printStackTrace();

        }
    }



    class TextAdapter extends BaseAdapter{


        List<String> list = new ArrayList<>();

        void setdata(List<String> mlist){
            list.clear();
            list.addAll(mlist);
            notifyDataSetChanged();
        }

        @Override
        public int getCount(){
            return list.size();
        }

        @Override
        public Object getItem(int position){
            return null;
        }

        @Override
        public long getItemId(int position){
            return 0;
        }

        @Override
        public View getView(int position, View convertview, ViewGroup parent){
            LayoutInflater Inflator = (LayoutInflater)
                    MainActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View roview = Inflator.inflate(R.layout.item, parent, false);
            TextView textView = roview.findViewById(R.id.one_task);

            textView.setText(list.get(position));

            return roview;
        }
    }

}
