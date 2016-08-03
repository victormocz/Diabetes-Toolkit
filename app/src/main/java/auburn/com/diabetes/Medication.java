package auburn.com.diabetes;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import data.ReadingDatabaseHandler;
import model.Medicationinfo;

/**
 * Created by victormo on 7/17/2016.
 */
public class Medication extends Fragment {
    private ListView medicationlist;
    private FloatingActionButton addfab;
    private Spinner intervalspinner;
    private ReadingDatabaseHandler db;
    private ArrayList<Medicationinfo> medications;
    private int listIndex = 0;
    private View currentIndexView;
    private MedicationAdapter ma;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initializeComponents();
    }


    private void initializeComponents() {
        addfab = (FloatingActionButton) getView().findViewById(R.id.addfab);
        addfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), AddMedication.class));
            }
        });


        db = new ReadingDatabaseHandler(getActivity());
        medications = db.getMedicationList();

        medicationlist = (ListView) getActivity().findViewById(R.id.allmedicationlist);
        ma = new MedicationAdapter();
        medicationlist.setAdapter(ma);

        medicationlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), MedicationDetail.class);
                intent.putExtra("id", medications.get(i).getId());
                intent.putExtra("name", medications.get(i).getName());
                intent.putExtra("form", medications.get(i).getForm());
                intent.putExtra("dosage", medications.get(i).getDosage());
                intent.putExtra("units", medications.get(i).getUnits());
                intent.putExtra("note", medications.get(i).getNote());
                intent.putExtra("startdate", medications.get(i).getStartdate());
                intent.putExtra("interval", medications.get(i).getInterval());
                intent.putExtra("lastdate", medications.get(i).getLasttakendate());
                intent.putExtra("istaken", medications.get(i).getIsTaken());
                startActivity(intent);
            }
        });

        medicationlist.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                listIndex = i;
                currentIndexView = view;
                Medicationinfo info = medications.get(listIndex);
                if (info == null) {
                    return false;
                }
                int taken = info.getIsTaken();
                AlertDialog.Builder test = new AlertDialog.Builder(getContext());
                if (taken == 0) {
                    test.setTitle("Mark as completed");
                    test.setMessage("Do you want to mark this as complete?");
                } else {
                    test.setTitle("Mark as incompleted");
                    test.setMessage("Do you want to mark this as incomplete?");
                }


                test.setIcon(android.R.drawable.ic_dialog_info);
                test.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Medicationinfo info = medications.get(listIndex);
                        if (info == null) {
                            return;
                        }
                        ImageView iv = (ImageView) currentIndexView.findViewById(R.id.todoimage);
                        TextView tv = (TextView) currentIndexView.findViewById(R.id.lastdate);
                        if (info.getIsTaken() != 0) {
                            info.setIsTaken(0);
                            info.setLasttakendate(0);
                            db.updateMedication(info.getId(), info);
                            iv.setImageResource(R.drawable.ic_todo);
                        } else if (info.getIsTaken() == 0) {
                            info.setIsTaken(1);
                            info.setLasttakendate(Calendar.getInstance().getTimeInMillis());
                            db.updateMedication(info.getId(), info);
                            iv.setImageResource(R.drawable.ic_done);
                            SimpleDateFormat simpleformat = new SimpleDateFormat("MM/dd/yyyy hh:mm aaa");
                            String datedata = simpleformat.format(new Date(Calendar.getInstance().getTimeInMillis()).getTime());
                            tv.setText("Last taken date: " + datedata);
                        }

                        db.updateMedication(info.getId(), info);
                        medications.set(listIndex, info);
                        medicationlist.invalidateViews();
                    }
                });
                test.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getContext(), "Have a nice day", Toast.LENGTH_SHORT).show();
                    }
                });

                test.show();
//                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                // continue with delete
//                            }
//                        })
//                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                // do nothing
//                            }
//                        })

                return true;
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.medication_layout, container, false);
    }

    private class MedicationAdapter extends BaseAdapter {

        private LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        @Override
        public int getCount() {
            return medications.size();
        }

        @Override
        public Medicationinfo getItem(int position) {
            return medications.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            view = inflater.inflate(R.layout.medication_row, null);
            TextView name = (TextView) view.findViewById(R.id.medname);
            TextView form = (TextView) view.findViewById(R.id.medform);
            TextView dosage = (TextView) view.findViewById(R.id.meddosage);
            TextView unit = (TextView) view.findViewById(R.id.medunit);
            TextView lastdate = (TextView) view.findViewById(R.id.lastdate);
            ImageView todo = (ImageView) view.findViewById(R.id.todoimage);

            Medicationinfo mdif = medications.get(position);

            name.setText(mdif.getName());
            form.setText(mdif.getForm());
            dosage.setText(Double.toString(mdif.getDosage()));
            unit.setText(mdif.getUnits());

            if (mdif.getIsTaken() == 0) {
                todo.setImageResource(R.drawable.ic_todo);
            } else {
                todo.setImageResource(R.drawable.ic_done);
            }

            if (mdif.getLasttakendate() == 0) {
                lastdate.setText("");
            } else {
                SimpleDateFormat simpleformat = new SimpleDateFormat("MM/dd/yyyy hh:mm aaa");
                String datedata = simpleformat.format(new Date(mdif.getLasttakendate()).getTime());
                lastdate.setText("Last taken date: " + datedata);
            }
            return view;
        }
    }
}
