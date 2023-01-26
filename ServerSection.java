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
	//Atributos estáticos- O atributo clientes é usado para armazenar o BufferedWriter de cada cliente conectado
		private static ArrayList<BufferedWriter>clientes;
		private static ServerSocket server; // Criação do servidor
		private String nome;
		private Socket con;
		private InputStream in;
		private InputStreamReader inr;
		private BufferedReader bfr;
		
		public ServerSection(Socket con){ // recebe um objeto socket como parâmetro e cria um objeto do tipo BufferedReader, que aponta para o stream do cliente socket
			   this.con = con;
			   try {
			         in  = con.getInputStream(); // Através daqui que consigo capturar o que é enviado
			         inr = new InputStreamReader(in); // Leio o que é enviado
			          bfr = new BufferedReader(inr); // crio um novo método para "escrita" do que é enviado.
			   } catch (IOException e) {
			          e.printStackTrace();
			   }
			}
			
			/**
			  * Método run
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
			 * Método usado para enviar mensagem para todos os clients
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
			   * Método main
			   * @param args
			   */
			public static void main(String []args) {

			  try{
			    //Cria os objetos necessário para instânciar o servidor
			    JLabel lblMessage = new JLabel("Porta do Servidor:");
			    JTextField txtPorta = new JTextField("12345"); //valor selecionado por padrão, (porta do server)
			    Object[] texts = {lblMessage, txtPorta};
			    JOptionPane.showMessageDialog(null, texts);
			    server = new ServerSocket(Integer.parseInt(txtPorta.getText())); // responsável por esperar a conexão do cliente, param=n° da porta
			    clientes = new ArrayList<BufferedWriter>();
			    JOptionPane.showMessageDialog(null,"Servidor ativo na porta: "+
			    txtPorta.getText());

			     while(true){
			       System.out.println("Aguardando conexão...");
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
