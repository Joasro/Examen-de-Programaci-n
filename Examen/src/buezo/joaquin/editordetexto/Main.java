package buezo.joaquin.editordetexto;

import javax.swing.*;
import javax.swing.text.DefaultEditorKit;
import javax.swing.undo.UndoManager;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


public class Main {
	public static void main(String [] args) {
	    Contorno contorno = new Contorno();
	    contorno.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    contorno.setVisible(true);
	}	
}
	
	
class Contorno extends JFrame{
	public Contorno() {
		setBounds(300,300,300,300);
		setTitle("Word 2 junto con Bloc de Notas");
		add(new Panel());
	}
}

class Panel extends JPanel{
	public Panel() {
			setLayout(new BorderLayout());
					
		//-----------Menu-------------------------
		JPanel  panelmenu = new JPanel();
		panelmenu.setLayout(new BorderLayout());
		Menu = new JMenuBar();
		archivo = new JMenu("Archivo");
		editar =  new JMenu("Editar");
		apariencia = new JMenu("Apariencia");
		onl =  new JMenu("Online");
		
		Menu.add(archivo);
		Menu.add(editar);
		Menu.add(apariencia);
		Menu.add(onl);
		
		///Menu-------------------------------------
		creaitem("Nuevo", "archivo", "narchivo");
		creaitem("Abrir...", "archivo", "abrir");
		creaitem("Guardar", "archivo", "guarda");
		creaitem("Guardar como", "archivo", "guardac");
		//--------------------------------------------
		
		//--------------------------------------------
		creaitem("Deshacer", "editar", "deshacer");
		creaitem("Restaurar", "editar", "rehacer");
		editar.addSeparator();
		creaitem("Copiar", "editar", "copiar");
		creaitem("Cortar", "editar", "cortar");
		creaitem("Pegar", "editar", "pegar");
		creaitem("Seleccionar Todo", "editar", "seleccion");
		//--------------------------------------------
		
		
		//--------------------------------------------
		creaitem("Normal", "apariencia", "normal");
		creaitem("Oscuro", "apariencia", "dark");
		//--------------------------------------------
		
		
		//--------------------------------------------
		creaitem("Abrir desde Internet", "onl", "");
	    //--------------------------------------------
				
		
		panelmenu.add(Menu, BorderLayout.NORTH);
		
		//**huevada fin-------------------------------
		
		
		//***textual****------------------------------
		elPane = new JTabbedPane(); 
		
		listFile = new ArrayList<File>();
		listAreaTexto = new ArrayList<JTextPane>();
		listScroll = new ArrayList<JScrollPane>();
		listManager = new ArrayList<UndoManager>();
		
		//**fin***------------------------
		
		//--------------------------------
		herramientas = new JToolBar(JToolBar.VERTICAL);
	    url = Main.class.getResource("/buezo/joaquin/img/marca-x.png");
	    Funciones.addButton(url, herramientas, "Cerra Pestaña").addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				int seleccion = elPane.getSelectedIndex();
				if(seleccion != -1) {
					listScroll.get(elPane.getSelectedIndex()).setRowHeader(null);
					elPane.remove(seleccion);
					listAreaTexto.remove(seleccion);
					listScroll.remove(seleccion);
					listManager.remove(seleccion);
					listFile.remove(seleccion);
					
					contadorp--;
					
					if(elPane.getSelectedIndex() == -1) {
						existePanel = false;
					}
				}
			}
	    	
	    });
	    
	    url = Main.class.getResource("/buezo/joaquin/img/mas (1).png");
	    Funciones.addButton(url, herramientas, "Nuevo Archivo").addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				creaPanel();
			}
	    	
	    });
		//-----------------------------------
	    
	    //--------------------panel-----------------------------
	    panelExtra = new JPanel();
	    panelExtra.setLayout(new BorderLayout());
	    
	    JPanel panelIzquierdo = new JPanel();
	    
	    JPanel panelCentro = new JPanel();
	    slider = new JSlider(8,38,14);
	    slider.setMajorTickSpacing(6);
		slider.setMinorTickSpacing(2);
	    
		slider.setPaintLabels(true);
		
		slider.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				// TODO Auto-generated method stub
				Funciones.tamTex(slider.getValue(), contadorp, listAreaTexto);
			}
			
		});
	    panelCentro.add(slider);
	    
	    panelExtra.add(panelIzquierdo, BorderLayout.WEST);
	    panelExtra.add(panelCentro, BorderLayout.EAST);
	    
		//------------------------------------------------------
	    
	    //--------------------------emerg------------------------
	    menuEmergente = new JPopupMenu();
	    
	    JMenuItem cortar = new JMenuItem("Cortar");
	    JMenuItem copiar = new JMenuItem("Copiar");
	    JMenuItem pegar = new JMenuItem("Pegar");
	    
	    cortar.addActionListener(new DefaultEditorKit.CutAction());
	    copiar.addActionListener(new DefaultEditorKit.CutAction());
	    pegar.addActionListener(new DefaultEditorKit.CutAction());
	    
	    menuEmergente.add(cortar);
	    menuEmergente.add(copiar);
	    menuEmergente.add(pegar);
	    
	    //------------------------------------------------------
		
	    
	    
		add(panelmenu,BorderLayout.NORTH);
		add(elPane, BorderLayout.CENTER);
		add(herramientas, BorderLayout.WEST);
		add(panelExtra, BorderLayout.SOUTH);
	}
	
	public void creaitem(String rot, String m, String ac) {
		eleI = new JMenuItem(rot);
		
		if(m.equals("archivo")) {
			archivo.add(eleI);
			if(ac.equals("narchivo")) {
				eleI.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						// TODO Auto-generated method stub
						creaPanel();
					}
				});
			}
			else if (ac.equals("abrir")) {
				eleI.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						// TODO Auto-generated method stub
						creaPanel();
						JFileChooser selectorArchivos = new JFileChooser();
						selectorArchivos.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
						int resultado = selectorArchivos.showOpenDialog(listAreaTexto.get(elPane.getSelectedIndex()));

						if (resultado == JFileChooser.APPROVE_OPTION) {
							try {
								boolean existePath = false;
								for (int i = 0; i < elPane.getTabCount(); i++) {
									File f = selectorArchivos.getSelectedFile();
									if (listFile.get(i).getPath().equals(f.getPath()))
										existePath = true;
								}
								if (!existePath) {
									File archivo = selectorArchivos.getSelectedFile();
									listFile.set(elPane.getSelectedIndex(), archivo);

									FileReader entrada = new FileReader(
											listFile.get(elPane.getSelectedIndex()).getPath());

									BufferedReader miBuffer = new BufferedReader(entrada);
									String linea = "";

									String titulo = listFile.get(elPane.getSelectedIndex()).getName();
									elPane.setTitleAt(elPane.getSelectedIndex(), titulo);

									while (linea != null) {
										linea = miBuffer.readLine();

										if (linea != null)
											Funciones.append(linea + "\n",
													listAreaTexto.get(elPane.getSelectedIndex()));

									}
									Funciones.aFondo(contadorp, tipoFondo, slider.getValue(), listAreaTexto);
								} else {

									for (int i = 0; i < elPane.getTabCount(); i++) {
										File f = selectorArchivos.getSelectedFile();
										if (listFile.get(i).getPath().equals(f.getPath())) {
											elPane.setSelectedIndex(i);

											listAreaTexto.remove(elPane.getTabCount() - 1);
											listScroll.remove(elPane.getTabCount() - 1);
											listFile.remove(elPane.getTabCount() - 1);
											elPane.remove(elPane.getTabCount() - 1);
											contadorp--;
											break;
										}
									}
								}
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						} else {

							int seleccion = elPane.getSelectedIndex();
							if (seleccion != -1) {
								listAreaTexto.remove(elPane.getTabCount() - 1);
								listScroll.remove(elPane.getTabCount() - 1);
								listFile.remove(elPane.getTabCount() - 1);
								elPane.remove(elPane.getTabCount() - 1);
								contadorp--;
							}
						}
					}
				});
			}
			else if(ac.equals("guarda")) {	
				eleI.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						// TODO Auto-generated method stub
						if(listFile.get(elPane.getSelectedIndex()).getPath().equals("")) {
							JFileChooser guardarArchivos = new JFileChooser();
							int opc = guardarArchivos.showSaveDialog(null);
							
							if(opc == JFileChooser.APPROVE_OPTION) {
								File archivo = guardarArchivos.getSelectedFile();
								listFile.set(elPane.getSelectedIndex(), archivo);
								elPane.setTitleAt(elPane.getSelectedIndex(), archivo.getName());
								
								try {
									FileWriter fw = new FileWriter(listFile.get(elPane.getSelectedIndex()).getPath());
									String texto = listAreaTexto.get(elPane.getSelectedIndex()).getText();
									
									for(int i = 0; i<texto.length(); i++) {
										fw.write(texto.charAt(i));
									}
									
									fw.close();
									
;								} catch (IOException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
							}
						}
						else {
							try {
								FileWriter fw = new FileWriter(listFile.get(elPane.getSelectedIndex()).getPath());
								String texto = listAreaTexto.get(elPane.getSelectedIndex()).getText();
								
								for(int i = 0; i<texto.length(); i++) {
									fw.write(texto.charAt(i));
								}
								
								fw.close();
								
;								} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
					}
					
				});
			}
			else if(ac.equals("guardac")) {
				eleI.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						// TODO Auto-generated method stub
						JFileChooser guardarArchivos = new JFileChooser();
						int opc = guardarArchivos.showSaveDialog(null);
						
						if(opc == JFileChooser.APPROVE_OPTION) {
							File archivo = guardarArchivos.getSelectedFile();
							listFile.set(elPane.getSelectedIndex(), archivo);
							elPane.setTitleAt(elPane.getSelectedIndex(), archivo.getName());
							
							try {
								FileWriter fw = new FileWriter(listFile.get(elPane.getSelectedIndex()).getPath());
								String texto = listAreaTexto.get(elPane.getSelectedIndex()).getText();
								
								for(int i = 0; i<texto.length(); i++) {
									fw.write(texto.charAt(i));
								}
								
								fw.close();
								
;								} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
					}
					
				});
			}
		}
		else if(m.equals("editar")) {
			editar.add(eleI);
			if(ac.equals("deshacer")) {
				eleI.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						// TODO Auto-generated method stub
						if(listManager.get(elPane.getSelectedIndex()).canUndo()) listManager.get(elPane.getSelectedIndex()).undo();
						
					}
					
				});
			}
			else if(ac.equals("rehacer")) {
				eleI.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						// TODO Auto-generated method stub
						if(listManager.get(elPane.getSelectedIndex()).canRedo()) listManager.get(elPane.getSelectedIndex()).redo();
					}
					
				});
			}
			else if(ac.equals("cortar")) {
				eleI.addActionListener(new DefaultEditorKit.CutAction());
			}
			else if(ac.equals("copiar")) {
				eleI.addActionListener(new DefaultEditorKit.CopyAction());
			}
			else if(ac.equals("pegar")) {
				eleI.addActionListener(new DefaultEditorKit.PasteAction());
			}
			else if(ac.equals("seleccion")) {
				eleI.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						// TODO Auto-generated method stub
						listAreaTexto.get(elPane.getSelectedIndex()).selectAll();
					}
					
				});
			}
		}
		else if(m.equals("apariencia")) {
			apariencia.add(eleI);
			if(ac.equals("normal")) {
				eleI.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						// TODO Auto-generated method stub
						tipoFondo="w";
						
						if(elPane.getTabCount() > 0)Funciones.aFondo(contadorp, tipoFondo,slider.getValue() ,listAreaTexto);
					}
					
				});
				
			}
			else if(ac.equals("dark")) {
				eleI.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						// TODO Auto-generated method stub
						tipoFondo="d";
						if(elPane.getTabCount() > 0) Funciones.aFondo(contadorp, tipoFondo,slider.getValue() ,listAreaTexto);
					}
					
				});
			}
		}
		else if(m.equals("onl")) {
			onl.add(eleI);
		}
		
	}
	
	public void creaPanel() {
		windowxd = new JPanel();
		windowxd.setLayout(new BorderLayout());
		listFile.add(new File(""));
		listAreaTexto.add(new JTextPane());
		listScroll.add(new JScrollPane(listAreaTexto.get(contadorp)));
		listManager.add(new UndoManager());
		
		listAreaTexto.get(contadorp).getDocument().addUndoableEditListener(listManager.get(contadorp));
		
		listAreaTexto.get(contadorp).setComponentPopupMenu(menuEmergente);
		
		windowxd.add(listScroll.get(contadorp), BorderLayout.CENTER);
		
		elPane.addTab("Titulo",windowxd);
		elPane.setSelectedIndex(contadorp);
		contadorp++;
		Funciones.aFondo(contadorp, tipoFondo,slider.getValue(), listAreaTexto);
		existePanel = true;
	}
	private static String tipoFondo = "w";
	private int contadorp = 0;
	private boolean existePanel = false;
	private JTabbedPane elPane;
	private JPanel windowxd;
	//private JTextPane areatextual;
	private ArrayList<JTextPane> listAreaTexto;
	private ArrayList<JScrollPane> listScroll;
	private ArrayList<UndoManager> listManager;
	private ArrayList<File> listFile;
	private JMenuBar Menu;
	private JMenu archivo, editar, apariencia, onl;
	private JMenuItem eleI;
	private JToolBar herramientas;
	private URL url; 
	private JPanel panelExtra;
	private JSlider slider;
	private JPopupMenu menuEmergente;
}