package communicationCommented;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;


public class ServerSection extends Thread {
	//Atributos est�ticos- O atributo clientes � usado para armazenar o BufferedWriter de cada cliente conectado
		private static ArrayList<BufferedWriter>clientes;
		private static ServerSocket server; // Cria��o do servidor
		private String nome;
		private Socket con;
		private InputStream in;
		private InputStreamReader inr;
		private BufferedReader bfr;
		
		public ServerSection(Socket con){ // recebe um objeto socket como par�metro e cria um objeto do tipo BufferedReader, que aponta para o stream do cliente socket
			   this.con = con;
			   try {
			         in  = con.getInputStream(); // Atrav�s daqui que consigo capturar o que � enviado
			         inr = new InputStreamReader(in); // Leio o que � enviado
			          bfr = new BufferedReader(inr); // crio um novo m�todo para "escrita" do que � enviado.
			   } catch (IOException e) {
			          e.printStackTrace();
			   }
			}
			
			/**
			  * M�todo run
			  */
			public void run(){

			  try{

			    String msg;
			    OutputStream ou =  this.con.getOutputStream();
			    Writer ouw = new OutputStreamWriter(ou);
			    BufferedWriter bfw = new BufferedWriter(ouw);
			    clientes.add(bfw);
			    nome = msg = bfr.readLine();

			    while(!"Sair".equalsIgnoreCase(msg) && msg != null)
			      {
			       msg = bfr.readLine();
			       sendToAll(bfw, msg);
			       System.out.println(msg);
			       }

			   }catch (Exception e) {
			     e.printStackTrace();

			   }
			}
			
			/***
			 * M�todo usado para enviar mensagem para todos os clients
			 * @param bwSaida do tipo BufferedWriter
			 * @param msg do tipo String
			 * @throws IOException
			 */
			public void sendToAll(BufferedWriter bwSaida, String msg) throws  IOException
			{
			  BufferedWriter bwS;

			  for(BufferedWriter bw : clientes){
			   bwS = (BufferedWriter)bw;
			   if(!(bwSaida == bwS)){
			     bw.write(nome + " -> " + msg+"\r\n");
			     bw.flush();
			   }
			  }
			}
			
			/***
			   * M�todo main
			   * @param args
			   */
			public static void main(String []args) {

			  try{
			    //Cria os objetos necess�rio para inst�nciar o servidor
			    JLabel lblMessage = new JLabel("Porta do Servidor:");
			    JTextField txtPorta = new JTextField("12345"); //valor selecionado por padr�o, (porta do server)
			    Object[] texts = {lblMessage, txtPorta};
			    JOptionPane.showMessageDialog(null, texts);
			    server = new ServerSocket(Integer.parseInt(txtPorta.getText())); // respons�vel por esperar a conex�o do cliente, param=n� da porta
			    clientes = new ArrayList<BufferedWriter>();
			    JOptionPane.showMessageDialog(null,"Servidor ativo na porta: "+
			    txtPorta.getText());

			     while(true){
			       System.out.println("Aguardando conex�o...");
			       Socket con = server.accept();
			       System.out.println("Cliente conectado...");
			       Thread t = new ServerSection(con);
			        t.start();
			    }

			  }catch (Exception e) {

			    e.printStackTrace();
			  }
			}
}
