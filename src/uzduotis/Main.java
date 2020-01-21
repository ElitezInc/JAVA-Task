package uzduotis;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Scanner;

public class Main 
{	
	static ArrayList<Preke> Prekes = new ArrayList<Preke>();
	static String Failas = "sample.csv";

	public static void main(String[] args) 
	{
		if(!Skaitymas(Prekes, "sample.csv")) return;
		
		while(true)
		{
			System.out.println("Programos galimi veiksmai:");
			System.out.println("1 - Trūkstamų prekių kiekio peržiūrėjimas.");
			System.out.println("2 - Prekių galiojimo laiko patikrinimas.");
			System.out.println("3 - Uždaryti programą.");
			System.out.print("\nĮveskite veiksmo pasirinkimą: ");
			int Pasirinkimas = (new Scanner(System.in)).nextInt();
			System.out.println();
			
			if (Pasirinkimas == 1)
			{
				System.out.print("Įveskite prekės kiekį, ieškoti mažesnio likučio prekių: ");
				int Kiekis = (new Scanner(System.in)).nextInt();
				System.out.println();
				
				System.out.println(Spausdinti(Trukumas(Kiekis)));
			}
			if (Pasirinkimas == 2)
			{
				Date Data = new Date();
				
				System.out.print("Įveskite datą, ieškoti prekių galiojimui iki jos (formatu YYYY-MM-DD): ");
				try { Data = new SimpleDateFormat("yyyy-MM-dd").parse((new Scanner(System.in)).nextLine()); } 
				catch (Exception Err) { Err.printStackTrace(); continue; }
				
				System.out.println(Spausdinti(Galiojimas(Data)));
			}
			if (Pasirinkimas == 3) break;
		}
	}
	
	static boolean Skaitymas(ArrayList<Preke> Prekes, String Failas)
	{		
		Scanner Skaitymas;
		
		try 
		{
			Skaitymas = new Scanner(new File(Failas));
			//Praleidžiama pirma failo eilutė
			Skaitymas.nextLine();
			
			while(Skaitymas.hasNext()) {
				//Skaitoma kiekviena failo eilutė ir yra atskiriami jos argumentai, juos naudojant sukuriant prekės objektus
			    String[] Args = Skaitymas.nextLine().split(",");
			    Preke preke = new Preke(Args[0], Args[1], Integer.parseInt(Args[2]), new SimpleDateFormat("yyyy-MM-dd").parse(Args[3]));
			    
			    //Pirma yra patikrinama ar egzistuoja panašus objektas saraše
			    //Jei rastas toks pat objektas, tai suradus jo poziciją saraše yra pasirenkamas objektas susumuojant jų kiekius
			    //Neradus panašaus objekto jis yra įdedamas į sąrašą.
			    if(!Prekes.contains(preke))
			    	Prekes.add(preke);
			    else
			    	Prekes.get(Prekes.indexOf(preke)).Kiekis += preke.Kiekis;
			}

			Skaitymas.close();
			Collections.sort(Prekes);
			System.out.println("Sėkmingai nuskaityti duomenys iš failo \"" + Failas + "\"\n");
		} 
		catch (Exception Err) {
			System.out.println("Failo \"" + Failas + "\" skaitymas nepavyko");
			Err.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	static String Spausdinti(ArrayList<Preke> Prekes)
	{
		String Tekstas = "";
		
		//Surašomas kiekvienas objektas lygiuotos lentelės formatu
		Tekstas += String.format("%-13s %-20s %-8s %s\n", "Pavadinimas", "Kodas", "Kiekis", "Galiojimas iki");
		Tekstas += "-----------------------------------------------------------\n";
		for(Preke preke : Prekes)
			Tekstas += preke.toString() + "\n";
		
		return Tekstas;
	}
	
	static ArrayList<Preke> Trukumas(int Kiekis)
	{
		ArrayList<Preke> Mazesni = new ArrayList<Preke>();
		
		for(Preke preke : Prekes)
			if(preke.Kiekis < Kiekis) Mazesni.add(preke);
		
		return Mazesni;
	}
	
	static ArrayList<Preke> Galiojimas(Date Data)
	{
		ArrayList<Preke> Pasibaige = new ArrayList<Preke>();
		
		for(Preke preke : Prekes)
			if(preke.Galiojimas.compareTo(Data) <= 0) Pasibaige.add(preke);
		
		return Pasibaige;
	}
}
	
class Preke implements Comparable<Preke>
{
	String Tipas;
	String Kodas;
	int Kiekis;
	Date Galiojimas;
	
	public Preke(String Tipas, String Kodas, int Kiekis, Date Galiojimas)
	{
		this.Tipas = Tipas;
		this.Kodas = Kodas;
		this.Kiekis = Kiekis;
		this.Galiojimas = Galiojimas;
	}
	
	//Objektų lyginimo metodas pagal klasės kintamuosius
	@Override
	public boolean equals(Object obj) {
		Preke Lyginama = (Preke) obj;
		if(Tipas.equals(Lyginama.Tipas) && Kodas.equals(Lyginama.Kodas) && Galiojimas.equals(Lyginama.Galiojimas))
			return true;
		return false;
	}
	
	//Rikiavimo metodas, kur nurodoma rikiuoti pagal tipo kintamojo alfabetiškumą
	@Override
	public int compareTo(Preke Lyginama) {
		return Tipas.compareTo(Lyginama.Tipas);
	} 
	
	//Objekto pavaizdavimas tekstu, pasitelkus formatavimą
	@Override
    public String toString() { 
        return String.format("%-13s %-20s %-8d %s", Tipas, Kodas, Kiekis, new SimpleDateFormat("yyyy-MM-dd").format(Galiojimas)); 
    }
}