import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Cronometro implements Runnable {

	private JFrame frame;
	JLabel tiempo;
	Thread hilo;
	JButton btnIniciar;
	JButton btnDetener;
	JButton btnReiniciar;
	private int minutos = 0;
	private int segundos = 0;
	private int milesimas = 0;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Cronometro window = new Cronometro();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Cronometro() {
		initialize();
	}

	public Cronometro(int minutos, int segundos, int milesimas) {
		this.minutos = minutos;
		this.segundos = segundos;
		this.milesimas = milesimas;
		this.suspendido.setSuspendido(false);
	}

	private SolicitarSuspender suspendido = new SolicitarSuspender();

	public SolicitarSuspender getSuspendido() {
		return suspendido;
	}

	public void setSuspendido(SolicitarSuspender suspendido) {
		this.suspendido = suspendido;
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 231);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JPanel panel = new JPanel();
		panel.setBounds(10, 11, 414, 111);
		frame.getContentPane().add(panel);
		panel.setLayout(null);

		tiempo = new JLabel("00:00:00");
		tiempo.setHorizontalAlignment(SwingConstants.CENTER);
		tiempo.setFont(new Font("Verdana", Font.PLAIN, 70));
		tiempo.setBounds(10, 11, 394, 89);
		panel.add(tiempo);

		btnDetener = new JButton("Detener\r\n");
		btnDetener.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					detenerCronometro();
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		});
		btnDetener.setFont(new Font("Verdana", Font.BOLD, 17));
		btnDetener.setBounds(156, 133, 120, 48);
		frame.getContentPane().add(btnDetener);

		btnIniciar = new JButton("Iniciar");
		btnIniciar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				iniciarCronometro();
			}
		});
		btnIniciar.setFont(new Font("Verdana", Font.BOLD, 17));
		btnIniciar.setBounds(10, 133, 120, 48);
		frame.getContentPane().add(btnIniciar);

		btnReiniciar = new JButton("Reiniciar\r\n");
		btnReiniciar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				reiniciarCronometro();
			}
		});
		btnReiniciar.setFont(new Font("Verdana", Font.BOLD, 17));
		btnReiniciar.setBounds(304, 133, 120, 48);
		frame.getContentPane().add(btnReiniciar);
	}

	public void run() {
		String min = "", seg = "", mil = "";
		try {
			while (!this.suspendido.getSuspendido()) {
				Thread.sleep(4);
				milesimas += 4;

				if (milesimas == 1000) {
					milesimas = 0;
					segundos += 1;

					if (segundos == 60) {
						segundos = 0;
						minutos += 1;
					}
				}

				if (minutos < 10)
					min = "0" + minutos;
				else
					min = "" + minutos;

				if (segundos < 10)
					seg = "0" + segundos;
				else
					seg = "" + segundos;
				if (milesimas < 10)
					mil = "0" + milesimas;
				else
					mil = "" + milesimas;
				tiempo.setText(min + ":" + seg + ":" + mil);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void iniciarCronometro() {
//		Cronometro c = new Cronometro(minutos, segundos, milesimas);
//		hilo = new Thread(c);
		hilo = new Thread(this);
		hilo.start();
		desactivarBoton(btnIniciar, "Contando", 14);
	}

	public void detenerCronometro() throws InterruptedException {
		if (!this.suspendido.getSuspendido()) {
			this.getSuspendido().setSuspendido(true);
			desactivarBoton(btnIniciar, "Detenido", 14);
			activarBoton(btnDetener, "Reanudar", 14);
		} else {
			this.getSuspendido().setSuspendido(false);
			iniciarCronometro();
			desactivarBoton(btnIniciar, "Contando", 14);
			activarBoton(btnDetener, "Detener", 17);
		}
	}

	public void reiniciarCronometro() {
		try {
			if (!hilo.isAlive()) {
				hilo.interrupt();
				this.getSuspendido().setSuspendido(false);
				tiempo.setText("00:00:00");
				minutos = 0;
				segundos = 0;
				milesimas = 0;
				activarBoton(btnIniciar, "Iniciar", 17);
				activarBoton(btnDetener, "Detener", 17);
			}
		} catch (Exception e) {
			System.err.println("No es posible reiniciar el cronómetro");
		}
	}

	public void desactivarBoton(JButton button, String texto, int tamano) {
		button.setEnabled(false);
		button.setText(texto);
		button.setFont(new Font("Verdana", Font.BOLD, tamano));
	}

	public void activarBoton(JButton button, String texto, int tamano) {
		button.setEnabled(true);
		button.setText(texto);
		button.setFont(new Font("Verdana", Font.BOLD, tamano));
	}

}
