package master.ejemplos.practicaandroid;

import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
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
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	private final int CONSULTAACTIVITY = 001;
	private final int INSERCIONACTIVITY = 002;
	private final int BORRARACTIVITY = 003;
	private final int MODIFICARACTIVITY = 004;
	
	private final int CONSULTA = 0;
	private final int BORRADO = 1;
	private final int MODIFICACION = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    
    public void consultar(View v){
    	new ConsultaBD(this, CONSULTA).execute();
    }
    
    public void insercion(View v){
    	new ConsultaBDInsercion(this).execute();
    }
    
    public void borrar(View v){
    	new ConsultaBD(this, BORRADO).execute();
    }
    
    public void edicion(View v){
    	new ConsultaBD(this, MODIFICACION).execute();
    }
    
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
		if(requestCode==CONSULTAACTIVITY){
			if(resultCode==RESULT_OK){
				Toast.makeText(this, "Consulta finalizada", Toast.LENGTH_SHORT).show();
			}
		}else if(requestCode == INSERCIONACTIVITY){
			if(resultCode==RESULT_OK){
				Toast.makeText(this, "Insercion finalizada con exito", Toast.LENGTH_SHORT).show();
			}
		} else if(requestCode == BORRARACTIVITY){
			if(resultCode==RESULT_OK){
				Toast.makeText(this, "Registro borrado", Toast.LENGTH_SHORT).show();
			} else if((resultCode==RESULT_CANCELED)){
				Toast.makeText(this, "Borrado cancelado", Toast.LENGTH_SHORT).show();
			}
		}else if(requestCode == MODIFICARACTIVITY){
			if(resultCode==RESULT_OK){
				Toast.makeText(this, "Registro modificado correctamente", Toast.LENGTH_SHORT).show();
			} else if((resultCode==RESULT_CANCELED)){
				Toast.makeText(this, "Modificacion cancelada", Toast.LENGTH_SHORT).show();
			}
		}
	}
    
	private class ConsultaBD extends AsyncTask<Void,Void,JSONArray>{
		private  final String DNI = "DNI";
		private  final String NOMBRE = "Nombre";
		private  final String APELLIDOS = "Apellidos";
		private  final String DIRECCION = "Direccion";
		private  final String TELEFONO = "Telefono";
		private  final String EQUIPO = "Equipo";
		
		private EditText text = (EditText)findViewById(R.id.editText1);
		private String dni = text.getText().toString();
		private JSONArray people = null;
		private Context context;
    	private ProgressDialog dialog;
    	private String URL = "http://demo.calamar.eui.upm.es/dasmapi/v1/miw39/fichas";
    	private int accion;
    	
    	public ConsultaBD(Context context, int accion){
    		this.context = context;
    		this.accion = accion;
    	}
    	
    	protected void onPreExecute(){
    		super.onPreExecute();
    		dialog = new ProgressDialog(MainActivity.this);
    		dialog.setMessage(getString(R.string.progress_title));
    		dialog.setIndeterminate(false);
    		dialog.setCancelable(true);
    		dialog.show();
    	}
    	@Override
		protected JSONArray doInBackground(Void... params) {
    		if(!dni.equals("")){
    			dni = "/"+dni;
    		}
    		String respuesta = "";
    		try {
				AndroidHttpClient client = AndroidHttpClient.newInstance("AndoidHtpClient");
				HttpGet get = new HttpGet(URL+dni);
				HttpResponse response = client.execute(get);
				respuesta = EntityUtils.toString(response.getEntity());
				if(respuesta.length()>15){
					people = new JSONArray(respuesta);
				}
				client.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				Log.e("BasicWebService", e.toString());
			}
    		return people;
		}
    	protected void onPostExecute(JSONArray param){
    		dialog.dismiss();
    		ArrayList<Persona> registros = new ArrayList<Persona>();
    		if(param != null){
    			try {
	    			for(int i=1; i<param.length(); i++){
	    				Persona persona = new Persona();
	    				JSONObject c =param.getJSONObject(i);
	    				persona.setDni(c.getString(DNI));
	    				persona.setNombre(c.getString(NOMBRE));
	    				persona.setApellidos(c.getString(APELLIDOS));
	    				persona.setDireccion(c.getString(DIRECCION));
	    				persona.setTelefono(c.getString(TELEFONO));
	    				persona.setEquipo(c.getString(EQUIPO));
	    				registros.add(persona);
	    			}    			
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    			Intent i = null;
    			int request = 0;
    			if(!dni.equals("")){
    				switch(accion){
	    			case CONSULTA: i = new Intent(context, ConsultaActivity.class);
						request = CONSULTAACTIVITY;break;
	    			case BORRADO: i = new Intent(context, BorrarActivity.class);
	    				request = BORRARACTIVITY;break;
	    			case MODIFICACION: i = new Intent(context, ModificarActivity.class);
	    				request = MODIFICARACTIVITY;break;
    				}
    				Bundle bundle = new Bundle(); 
		    		bundle.putSerializable("registros", registros);
		    		i.putExtras(bundle);
		    		startActivityForResult(i, request);
    			}else{
    				switch(accion){
	    			case CONSULTA: i = new Intent(context, ConsultaActivity.class);
						request = CONSULTAACTIVITY;
						Bundle bundle = new Bundle(); 
			    		bundle.putSerializable("registros", registros);
			    		i.putExtras(bundle);
			    		startActivityForResult(i, request);
						break;
	    			case BORRADO: Toast.makeText(context, "Introducir un dni", Toast.LENGTH_SHORT).show();break;
	    			case MODIFICACION: 	Toast.makeText(context, "Introducir un dni", Toast.LENGTH_SHORT).show();;break;
    				}
    			}
    		}else{
    			Intent i = new Intent(context, InsertarActivity.class);
	    		i.putExtra("dni", dni.split("/")[1]);
	    		startActivityForResult(i, INSERCIONACTIVITY);    		
    		}
    	}		
    }
	
	private class ConsultaBDInsercion extends AsyncTask<Void,Void,JSONArray>{
		
		private EditText text = (EditText)findViewById(R.id.editText1);
		private String dni = text.getText().toString();
		private Context context;
    	private ProgressDialog dialog;
    	private String URL = "http://demo.calamar.eui.upm.es/dasmapi/v1/miw39/fichas/";
    	private JSONArray people = null;
    	
    	public ConsultaBDInsercion(Context context){
    		this.context = context;
    	}
    	
    	protected void onPreExecute(){
    		super.onPreExecute();
    		dialog = new ProgressDialog(MainActivity.this);
    		dialog.setMessage(getString(R.string.progress_title));
    		dialog.setIndeterminate(false);
    		dialog.setCancelable(true);
    		dialog.show();
    	}
    	@Override
		protected JSONArray doInBackground(Void... params) {
    		String respuesta = "";
    		if(!dni.equals("")){
    			try {
					AndroidHttpClient client = AndroidHttpClient.newInstance("AndoidHtpClient");
					HttpGet get = new HttpGet(URL+dni);
					HttpResponse response = client.execute(get);
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
    		
    		return people;
		}
    	protected void onPostExecute(JSONArray param){
    		dialog.dismiss();
    		if(param == null && !dni.isEmpty()){
	    		Intent i = new Intent(context, InsertarActivity.class);
	    		i.putExtra("dni", dni);
	    		startActivityForResult(i, INSERCIONACTIVITY);
    		}else{
				Toast.makeText(context, "DNI ya existente", Toast.LENGTH_SHORT).show();
    		}
    		
    	}		
    }
}
