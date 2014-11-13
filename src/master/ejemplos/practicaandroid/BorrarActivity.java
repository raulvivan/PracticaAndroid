package master.ejemplos.practicaandroid;

import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;

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
import android.widget.Toast;

public class BorrarActivity extends Activity {
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
		setContentView(R.layout.activity_borrar);
		
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
	
	public void borrar(View v){
		new ConsultaBDBorrar(this).execute();
	}
	
	public void onBackPressed(){
		setResult(RESULT_CANCELED);
		super.onBackPressed();
	}
	
	private class ConsultaBDBorrar extends AsyncTask<Void,Void,Void>{
		
		private EditText text = (EditText)findViewById(R.id.editText1);
		private String dni = text.getText().toString();
		private Context context;
    	private ProgressDialog dialog;
    	private String URL = "http://demo.calamar.eui.upm.es/dasmapi/v1/miw39/fichas/";
    	private JSONArray people = null;
    	
    	public ConsultaBDBorrar(Context context){
    		this.context = context;
    	}
    	
    	protected void onPreExecute(){
    		super.onPreExecute();
    		dialog = new ProgressDialog(BorrarActivity.this);
    		dialog.setMessage(getString(R.string.progress_title));
    		dialog.setIndeterminate(false);
    		dialog.setCancelable(true);
    		dialog.show();
    	}
    	@Override
		protected Void doInBackground(Void... params) {
    		String respuesta = "";
    		if(!dni.equals("")){
    			try {
					AndroidHttpClient client = AndroidHttpClient.newInstance("AndoidHtpClient");
					HttpDelete delete = new HttpDelete(URL+dni);
					HttpResponse response = client.execute(delete);
					respuesta = EntityUtils.toString(response.getEntity());
					if(respuesta.length()>15){
						people = new JSONArray(respuesta);
					}
					client.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					Log.e("BasicWebService", e.toString());
				}
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
