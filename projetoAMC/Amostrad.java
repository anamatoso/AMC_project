package projetoAMC;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

class paciente implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	int [] parametros; 	//medicoes
	paciente next;		//proximo paciente

	public paciente(int[] parametros) { //iniciaçao do paciente com os parametros dele e sem next
		super();
		this.parametros = parametros;
		this.next = null;
	}

	public String toString() {
		return "\n" + Arrays.toString(parametros) +","+ next; 
	}
}

public class Amostrad implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	int length;			//numero de pacientes
	int npar;			//numero de parametros/mediçoes
	paciente first;		//primeiro paciente da amostra
	paciente last;		//ultimo paciente da amostra
	ArrayList<Integer> dominio;

	public Amostrad() {	//iniciaçao da amostra começa com ela vazia
		this.length = 0;
		this.npar = 0;
		this.first = null;
		this.last = null;
	}

	public Amostrad(String filename) throws Exception {

		BufferedReader br = null;
		String line       = "";
		String csvSplit   = ",";
		this.length = 0;
		this.npar = 0;
		this.first = null;
		this.last = null;

		try {
			// Criar um buffer para o descritor do ficheiro com nome filename
			br = new BufferedReader(new FileReader(filename));
			try {
				line=br.readLine();
				String[] parametrosf = line.split(csvSplit); 
				this.npar=line.split(csvSplit).length; // Ler a primeira linha e ver numero de parametros
				int parf[]=new int [npar];
				//				this.dominio=new ArrayList<Integer>(npar);
				for (int i = 0; i < parf.length; i++) {parf[i]=Integer.parseInt(parametrosf[i]);} //meter parametros num vetor
				first=new paciente(parf); //indico o primeiro paciente
				add(first); //adiciono o a amostra

				while ( (line = br.readLine()) != null ) { // Enquanto ha linhas menos a ultima
					String[] parametros = line.split(csvSplit); // Dividir linha por , comprimento=numero de mediçoes
					int p[]=new int [npar];
					for (int i = 0; i < p.length; i++) {p[i]=Integer.parseInt(parametros[i]);}
					add(new paciente(p)); //adiciono o novo paciente com os parametros da linha
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String toString() {
		return "Amostra [len=" + length + ", {"+first+"}]";
	}

	private boolean emptyQ() { //se a amostra esta vazia
		return length==0;
	}

	public void add(paciente novopac) throws Exception{	//adiciona paciente
		if (emptyQ()) { //se estiver vazia
			first=novopac;						//o primeiro paciente é o novo
			last=novopac;						//o ultimo paciente é o novo
			npar=novopac.parametros.length;		//numero de mediçoes passa a ser o do paciente novo
			length++;							//aumento comprimento da amostra em 1 paciente
			dominio = new ArrayList<Integer>(npar);
			for (int par : novopac.parametros) {dominio.add(par+1);}
		}
		else {			//se amostra ja tiver pacientes
			if (npar==novopac.parametros.length) { 	//se o novo paciente tiver o msm numero de mediçoes que os que ja la estao
				last.next=novopac;					//o next do ultimo é o que vamos inserir
				last=novopac;						//o ultimo passa a ser o que vamos inserir
				length++;							//aumento comprimento da amostra em 1 paciente
				for (int i=0; i < this.npar; i++) {	//atualizr dominio
					if (novopac.parametros[i]+1>dominio.get(i)) {
						dominio.remove(i);
						dominio.add(i, novopac.parametros[i]+1);
					}
				}
			}
			else 
				throw new Exception("Erro: Tamanho do vetor não coincide com o número de parâmtros da amostra na funçao add");
		} 
	}

	public int length() {
		return length;
	}

	public int[] element(int pos) throws Exception{	//retorna o vetor de uma certa mediçao/parametro
		if (pos>=0 && pos<length) {	
			paciente x=first;
			int i=0;
			while (i<pos) {
				x=x.next;
				i++;
			}
			return x.parametros;
		}
		else { 
			throw new Exception("Erro: Posiçao out of bounds na funçao element");}
	}


	public int domain(int vpos[]) { //dominio de um conjunto de parametros que esto nas posiçoes do vpos
		int res=1;
		for (int pos : vpos) {
			res=res*(dominio.get(pos));
		}

		return res;
	}

	public double count(int vpos[], int vval[]) {return countp(first,vpos,vval);}

	private double countp(paciente p, int[] vpos, int[] vval) {
		if (p==null) return 0;
		else {
			boolean dif= false; //diferenca entre valores
			int i =0;
			while ( i<vpos.length && !dif) {
				if (p.parametros[vpos[i]]!=vval[i]) dif=true;
				i++;
			}
			if (dif) return countp(p.next,vpos,vval);
			else {return 1+countp(p.next,vpos,vval);}
		}
	}

	public static void main(String[] args) {
		try {
			Amostrad a = new Amostrad("bcancer.csv");
			System.out.println(a.toString());}

		catch(Exception e){e.printStackTrace();}
		//		Amostrad a=new Amostrad();
		//		
		//		int v1[]= {0,0,3};
		//		paciente p1= new paciente(v1);
		//		try { a.add(p1);} catch(Exception e){e.printStackTrace();}
		//
		//		int v2[]= {0,1,0};
		//		paciente p2= new paciente(v2);
		//		try { a.add(p2);} catch(Exception e){e.printStackTrace();}
		//
		//		int v3[]= {1,0,1};
		//		paciente p3= new paciente(v3);
		//		try { a.add(p3);} catch(Exception e){e.printStackTrace();}
		//		
		//		System.out.println(a.toString());
		//		
		//		int vpos[]= {0,2};
		//		int vval[]= {1,1};
		//		
		//		System.out.println("");
		//		try { System.out.println(Arrays.toString(a.element(1)));} catch(Exception e){e.printStackTrace();}
		//		System.out.println("");
		//		try { System.out.println(a.domain(vpos));} catch(Exception e){e.printStackTrace();}
		//		System.out.println("");
		//		System.out.println(a.count(vpos,vval));

	}
}

