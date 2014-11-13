package master.ejemplos.practicaandroid;

import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class ModificarActivity extends Activity {
	private TextView text;
	private EditText textDNI;
	private EditText textNombre;
	private EditText textApellidos;
	private EditText textDireccion;
	private EditText textTelefono;
	private EditText textEquipo;
	
	private ArrayList<Persona> registros = new ArrayList<Persona>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_modificar);
		
		Bundle extras = getIntent().getExtras();
		registros = (ArrayList<Persona>) extras.getSerializable("registros");
		
		
		textDNI = (EditText)findViewById(R.id.editText1);
		textNombre = (EditText)findViewById(R.id.editText2);
		textApellidos = (EditText)findViewById(R.id.editText3);
		textDireccion = (EditText)findViewById(R.id.editText4);
		textTelefono = (EditText)findViewById(R.id.editText5);
		textEquipo = (EditText)findViewById(R.id.editText6);
		
		textDNI.setText(registros.get(0).getDni());
		textNombre.setText(registros.get(0).getNombre());
		textApellidos.setText(registros.get(0).getApellidos());
		textDireccion.setText(registros.get(0).getDireccion());
		textTelefono.setText(registros.get(0).getTelefono());
		textEquipo.setText(registros.get(0).getEquipo());
	}
	
	public void modificar(View v){
		new ConsultaBDModificar(this).execute();
	}
	
	public void onBackPressed(){
		setResult(RESULT_CANCELED);
		super.onBackPressed();
	}
	
	private class ConsultaBDModificar extends AsyncTask<Void,Void,Void>{
		private  final String DNI = "DNI";
		private  final String NOMBRE = "Nombre";
		private  final String APELLIDOS = "Apellidos";
		private  final String DIRECCION = "Direccion";
		private  final String TELEFONO = "Telefono";
		private  final String EQUIPO = "Equipo";

		private Context context;
    	private ProgressDialog dialog;
    	private String URL = "http://demo.calamar.eui.upm.es/dasmapi/v1/miw39/fichas";
    	
    	public ConsultaBDModificar(Context context){
    		this.context = context;
    	}
    	
    	protected void onPreExecute(){
    		super.onPreExecute();
    		dialog = new ProgressDialog(ModificarActivity.this);
    		dialog.setMessage(getString(R.string.progress_title));
    		dialog.setIndeterminate(false);
    		dialog.setCancelable(true);
    		dialog.show();
    	}
    	@Override
		protected Void doInBackground(Void... params) {
    		JSONObject object = new JSONObject();
    		try {
				object.put(DNI, ((EditText)findViewById(R.id.editText1)).getText().toString());
				object.put(NOMBRE, ((EditText)findViewById(R.id.editText2)).getText().toString());
				object.put(APELLIDOS, ((EditText)findViewById(R.id.editText3)).getText().toString());
				object.put(DIRECCION, ((EditText)findViewById(R.id.editText4)).getText().toString());
				object.put(TELEFONO, ((EditText)findViewById(R.id.editText5)).getText().toString());
				object.put(EQUIPO, ((EditText)findViewById(R.id.editText6)).getText().toString());
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
    		try {
				AndroidHttpClient client = AndroidHttpClient.newInstance("AndoidHtpClient");
				HttpPut put= new HttpPut(URL);
				StringEntity entity = new StringEntity(object.toString(), HTTP.UTF_8);
				put.setEntity(entity);
				put.setHeader("Accept", "application/json");
	            put.setHeader("Content-type", "application/json");
				HttpResponse response = client.execute(put);
				client.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				Log.e("BasicWebService", e.toString());
			}
    		return null;
		}
    	protected void onPostExecute(Void param){
    		dialog.dismiss();
    		Intent i = new Intent(context, MainActivity.class);
    		setResult(RESULT_OK, i);
    		finish();
    	}		
    }
}
