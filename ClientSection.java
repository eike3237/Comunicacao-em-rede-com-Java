package communicationCommented;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DefaultCaret;

import java.awt.Font;
import javax.swing.ScrollPaneConstants;

public class ClientSection extends JFrame implements ActionListener, KeyListener{

	//private JPanel contentPane;
	private JFrame frame;
	
	// Atributos fora os de Texto
		private Socket socket; // Atributo para receber o cliente
		private OutputStream ou; // Atributo preparado para receber o tipo outputStream q envia dados para o outro lado
		private Writer ouwriter; // ?????????? Escritor
		private BufferedWriter bfw; // Buffer para escrever
		
		private JTextField txtIp;
		private JTextField txtPorta;
		private JTextField txtNome;
		
		//Atributos que são variaveis graficas
		private JPanel pnlContent;
		private JLabel lblMsg; 
		private JButton btnSend;
		private JButton btnSair;
		private JLabel lblHistorico;
		private JTextField txtMsg;
		private JTextArea texto;
		private static final long serialVersionUID = 1L;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) throws IOException {
		
		ClientSection frame = new ClientSection();
		
		frame.conectar();
		frame.escutar(); // Problema no window.escutar
		//frame.setVisible(true);
				
	}

	/**
	 * Create the frame.
	 */
	public ClientSection() throws IOException {
		initialize();
		
		/*setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);*/
	}
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		// Primeira tela 
		JLabel lblMessage = new JLabel("Verificar!");
		txtIp = new JTextField("127.0.0.1");
		txtPorta = new JTextField("12345");
		txtNome = new JTextField("Cliente");
		Object[] texts = {lblMessage, txtIp, txtPorta, txtNome};
		JOptionPane.showMessageDialog(null, texts);
		// Primeira tela
		
		frame = new JFrame();
		frame.setResizable(false);
		frame.setBounds(100, 100, 474, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setTitle(txtNome.getText()); // Colocando o nome do usuario 
		
		frame.setLocationRelativeTo(null); // Do site
		frame.setVisible(true);
		
		pnlContent = new JPanel(); // !!!! * COMENTARIOS COMO ESTE É SOMENTE PARA CHECAGEM DA CRIAÇÃO DE ATRIBUTO, IGNORAR
		pnlContent.setBackground(Color.GRAY);
		pnlContent.setBounds(6, 0, 462, 263);
		frame.getContentPane().add(pnlContent);
		pnlContent.setLayout(null);
		
		txtMsg = new JTextField(20); // !!!! 
		txtMsg.setFont(new Font("Tahoma", Font.PLAIN, 12));
		txtMsg.addKeyListener(this); //Listener de ativação para o própio txtMsg
		txtMsg.setBounds(6, 214, 347, 28);
		txtMsg.setColumns(10);
		pnlContent.add(txtMsg);
		
		btnSend = new JButton("Send"); // !!!! *
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				/*
				 * método usado para receber as ações dos botões dos usuários,
				 * Nele foi feito um chaveamento: se o usuário pressionar o botão “send” 
				 * então será enviada uma mensagem, senão será encerrado o chat.
				 * */
				try { 
					if(arg0.getActionCommand().equals(btnSend.getActionCommand())) {
						enviarMensagem(txtMsg.getText());
					} else {
						if(arg0.getActionCommand().equals(btnSair.getActionCommand())) {
							sair();
						}
					}
				} catch (IOException e1) {
					
				}
			}
		});
		btnSend.setBounds(371, 214, 74, 28);
		btnSend.setToolTipText("Enviar Mensagem"); // mensagem de dica ao deixar o mouse em cima
		btnSend.addActionListener(this); // Listener de ativação do botão
		pnlContent.add(btnSend);
		
		btnSair = new JButton("Sair"); // !!!! *
		btnSair.setToolTipText("Sair do chat");
		btnSair.addActionListener(this); //Listener de ativação do botão pra escutar
		btnSair.setBounds(371, 29, 74, 28);
		pnlContent.add(btnSair);
		
		lblHistorico = new JLabel("Hist\u00F3rico"); // !!!! *
		lblHistorico.setBounds(147, 5, 49, 16);
		pnlContent.add(lblHistorico);
		
		lblMsg = new JLabel("Mensagem"); // !!!! *
		lblMsg.setBounds(147, 197, 62, 16);
		pnlContent.add(lblMsg);
		
		texto = new JTextArea();
		texto.setFont(new Font("Monospaced", Font.BOLD, 14));
		texto.setLineWrap(true);
		texto.setEditable(false);
		texto.setBackground(SystemColor.menu);
		texto.setLineWrap(true); // Quebra de linha automatica
		
		JScrollPane scroll = new JScrollPane(texto);
		scroll.setBounds(6, 30, 341, 165);
		pnlContent.add(scroll);
		scroll.setViewportView(texto);
		
		// auto-scroll
		DefaultCaret caret = (DefaultCaret)texto.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		
		//2 Linhas abaixo teste, pode tirar
		//texto.setBorder(BorderFactory.createEtchedBorder(Color.BLUE, Color.BLUE));
	    //txtMsg.setBorder(BorderFactory.createEtchedBorder(Color.BLUE, Color.BLUE));
	    
	    frame.setTitle(txtNome.getText()); // Trocando o nome da janela para o nome do cara
	    //setContentPane(pnlContent); // Tentar desativar o outro ContentPane acima
	    frame.setResizable(false); //qlqr coisa tirar o frame
	    //setSize()
	    //setVisible = Já está feito no main
	    frame.setVisible(true);
		
	}
	
	// Método usado para conectar no server socket, retorna IO Exception caso dê algum erro.
	public void conectar() throws IOException {
		socket = new Socket(txtIp.getText(), Integer.parseInt(txtPorta.getText())); /* conectando, no IP armazenado no atributo,
		 e de segundo parametro a porta. */
		ou = socket.getOutputStream(); // pra enviar a informação pro outro lado, no caso o server.
		ouwriter = new OutputStreamWriter(ou); // instanciando o escrivão feito no atributo da classe.
		bfw = new BufferedWriter(ouwriter); // Buffer de escrita criado passando como parametro, o OutputStreamWriter
		bfw.write(txtNome.getText() + "\r\n"); // escrevendo via buffer o nome do cara conectado
		bfw.flush(); // ??????????? Ver o que é o Método flush
	}
	
	/* método usado para enviar mensagens do cliente para o servidor socket. 
	 * Assim, toda vez que ele escrever uma mensagem e apertar o botão “Enter”, esta será enviada para o servidor.
	 * Recebe como parâmetro uma string, que é a mensagem a ser enviado */
	public void enviarMensagem(String msg) throws IOException {
		if(msg.equals("Sair")) {
			bfw.write("Desconectado \r\n"); // Usando o buffer para escrever...
			texto.append("Desconectado \r\n"); // Escrevendo no JTextArea adicionando o texto no fim
		} else {
			//Else caso ele não tenha dado o comando para sair
			bfw.write(msg + "\r\n"); // escreve a mensagem recebida, através do buffer
			texto.append(txtNome.getText() + ":\n" + txtMsg.getText() + "\r\n");
			// Acima acrescento na parte de mensagens, o nome do cara e abaixo a mensagem que ele mandou
		}
		bfw.flush(); // ????????? Ainda tenho q ver o q é flush
		txtMsg.setText(""); // Agora que já foi enviada a mensagem eu zero a próxima.
	}
	
	/* método usado para escutar (receber) mensagens do servidor. Toda vez que alguém enviar uma, 
	 * o método será processado pelo servidor e envia para todos os clientes conectados, por isso a necessidade do código.*/
	public void escutar() throws IOException {
		
		InputStream in = socket.getInputStream(); // Tipo de classe pra capturar o que cliente está enviando.
		InputStreamReader inr = new InputStreamReader(in); // O leitor da classe InputStream
		BufferedReader bfr = new BufferedReader(inr); // Leitor em buffer para a mensagem capturada pelo server do cliente.
		String msg = ""; // Variavel mensagem que receberá a mensagem lida pelo outro lado do server.
		
		while(!"Sair".equalsIgnoreCase(msg))  // Ignora enquanto elas não forem iguais "Eu Acho"
			
			if(bfr.ready()) { // Se o buffer ta pronto para ler
				msg = bfr.readLine(); // Le a mensagem enviada pelo outro lado e lido pelo buffer e armazena na var. msg
				if(msg.equals("Sair")) { // Caso a mensagem seja pra sair
					texto.append("Servidor caiu! \r\n"); // msg de alerta que o server caiu
				} else { // Caso não seja pra sair só escreve a mensagem
					texto.append(msg + "\r\n");
				}
			}
		
		
	}
	
	// O método abaixo é usado para desconectar do server socket. Nele o sistema apenas fecha os streams de comunicação.
	// É ativado quando o cliente clica em "Sair"
	public void sair() throws IOException {
				
		enviarMensagem("Sair");
		bfw.close();
		ouwriter.close();
		ou.close();
		socket.close();
	}
	
	// BUTÃO
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		try {
			if(e.getActionCommand().equals(btnSend.getActionCommand()))
				enviarMensagem(txtMsg.getText());
			else
				if(e.getActionCommand().equals(btnSair.getActionCommand()))
					sair();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	// O metódo abaixo é acionado quando o usuário pressiona “Enter”, 
	// verificando se o key code é o Enter. Caso seja, a mensagem é enviada para o servidor.
	public void keyPressed(KeyEvent e) {

	    if(e.getKeyCode() == KeyEvent.VK_ENTER){
	       try {
	          enviarMensagem(txtMsg.getText());
	       } catch (IOException e1) {
	           // TODO Auto-generated catch block
	           e1.printStackTrace();
	       }
	   }
	}
	
	
	//@Override
	   public void keyReleased(KeyEvent arg0) {
	     // TODO Auto-generated method stub
	   }

	   //@Override
	   public void keyTyped(KeyEvent arg0) {
	     // TODO Auto-generated method stub
	   }

}
