package br.ufc.quixada.dadm.fragmentsquixada;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class ListFragment extends Fragment {

    private ArrayList<String> listaElementos;
    public ArrayAdapter<String> mAdapter;
    ListView listView;
    View view;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        view = inflater.inflate(R.layout.list_fragment, container, false);
        return view;
    }

    public void onActivityCreated(Bundle savedInstanceState) {

        listaElementos = new ArrayList<String>();
        listView = (ListView) view.findViewById( R.id.lvitemList );

        super.onActivityCreated(savedInstanceState);
        mAdapter = new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                listaElementos);

        listView.setAdapter( mAdapter );

    }

    public void addString( String element ){
        listaElementos.add( element );
        mAdapter.notifyDataSetChanged();
        Toast.makeText( getActivity(), element, Toast.LENGTH_LONG ).show();
    }
}
