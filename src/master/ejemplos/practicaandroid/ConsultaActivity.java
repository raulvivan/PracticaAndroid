package master.ejemplos.practicaandroid;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ConsultaActivity extends Activity {

	private int posicion = 1;
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
		setContentView(R.layout.activity_consulta);
		
		Bundle extras = getIntent().getExtras();
		registros = (ArrayList<Persona>) extras.getSerializable("registros");
		
		text = (TextView)findViewById(R.id.textView2);
		text.setText("Registro "+posicion+" de "+registros.size());
		
		textDNI = (EditText)findViewById(R.id.editText1);
		textNombre = (EditText)findViewById(R.id.editText2);
		textApellidos = (EditText)findViewById(R.id.editText3);
		textDireccion = (EditText)findViewById(R.id.editText4);
		textTelefono = (EditText)findViewById(R.id.editText5);
		textEquipo = (EditText)findViewById(R.id.editText6);
		
		cambiarCampos();
		
		ocultarAnterior();
		if(registros.size() == 1){
			ocultarSiguiente();
		}
	}
	
	private void ocultarAnterior(){
		findViewById(R.id.ImageButton02).setEnabled(false);
		findViewById(R.id.ImageButton03).setEnabled(false);
	}
	
	private void mostrarAnterior(){
		findViewById(R.id.ImageButton02).setEnabled(true);
		findViewById(R.id.ImageButton03).setEnabled(true);
	}
	
	private void ocultarSiguiente(){
		findViewById(R.id.imageButton1).setEnabled(false);
		findViewById(R.id.ImageButton01).setEnabled(false);
	}
	
	private void mostrarSiguiente(){
		findViewById(R.id.imageButton1).setEnabled(true);
		findViewById(R.id.ImageButton01).setEnabled(true);
	}
	
	private void cambiarCampos(){
		textDNI.setText(registros.get(posicion-1).getDni());
		textNombre.setText(registros.get(posicion-1).getNombre());
		textApellidos.setText(registros.get(posicion-1).getApellidos());
		textDireccion.setText(registros.get(posicion-1).getDireccion());
		textTelefono.setText(registros.get(posicion-1).getTelefono());
		textEquipo.setText(registros.get(posicion-1).getEquipo());
	}
	
	public void siguiente(View v){
		posicion++;
		cambiarCampos();
		text.setText("Registro "+posicion+" de "+registros.size());
		if(posicion == registros.size()){
			ocultarSiguiente();
		}else if(posicion == 2){
			mostrarAnterior();
		}
	}
	
	public void anterior(View v){
		posicion--;
		cambiarCampos();
		text.setText("Registro "+posicion+" de "+registros.size());
		if(posicion == 1){
			ocultarAnterior();
		}else if(posicion == registros.size()-1){
			mostrarSiguiente();
		}
	}
	
	public void primero(View v){
		posicion = 1;
		cambiarCampos();
		text.setText("Registro "+posicion+" de "+registros.size());
		ocultarAnterior();
		mostrarSiguiente();
	}
	
	public void ultimo(View v){
		posicion = registros.size();
		cambiarCampos();
		text.setText("Registro "+posicion+" de "+registros.size());
		ocultarSiguiente();
		mostrarAnterior();
	}
	
	public void onBackPressed(){
		setResult(RESULT_OK);
		super.onBackPressed();
	}
}
