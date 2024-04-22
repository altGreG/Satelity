package OtherGroupsWork.LyngSat;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import scraper.WebsiteData.Satellite.Transmitter;
import scraper.WebsiteData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LyngSat extends WebsiteData{


	public List<WebsiteData.Satellite> ScrapeLyngSat() throws IOException {
		ArrayList<WebsiteData.Satellite> satellites = new ArrayList<WebsiteData.Satellite>();
		
		String baseURL = "http://www.lyngsat.com/";
		String[] sublinks = {"asia", "europe", "atlantic", "america"};
		ArrayList<String> satLinks = new ArrayList<String>();
		for (String sub : sublinks) {
			try {
				Document d = Jsoup.connect(baseURL + sub + ".html").userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64; pl-PL; rv:120.0) Gecko/20100101 Firefox/120.0")
						.referrer("http://www.google.com").timeout(60000).execute().parse();
				Elements links = d.body().getElementsByAttributeValue("align", "center").first().getElementsByTag("a");
				for (Element link : links) {
					String href =  link.attr("href");
					if (!href.contains("/")) {
						if (satLinks == null || !(satLinks.contains(href))) {
							satLinks.add(href);
						}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
				throw e;
			}
		}

		int counter = 0;
		for (String sat : satLinks) {
			System.out.write(("\rLyngSat: " + counter + "\n").getBytes());
			counter++;

			String satName = null;
			Float satActPos = null;
			WebsiteData.Satellite thisSat = new WebsiteData.Satellite();
			
			Document d = Jsoup.connect(baseURL + sat).userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64; pl-PL; rv:120.0) Gecko/20100101 Firefox/120.0")
					.referrer("http://www.google.com").timeout(60000).execute().parse();
			Elements tables = d.body().getElementsByClass("bigtable").first().getElementsByAttributeValue("width", "728").first().children();
			
			String[] satelliteName = tables.get(1).text().split(" at ");
			for(int i=0; i<satelliteName.length; i++)
			{
				satelliteName[i] = satelliteName[i].strip();
			}
			
			//System.out.println(satelliteName[0] + " " + satelliteName[1]);

			satName = satelliteName[0];
			satActPos = (satelliteName[1].split("°")[1].equals("E") ? Float.parseFloat(satelliteName[1].split("°")[0])
					: (-1)*Float.parseFloat(satelliteName[1].split("°")[0]));
			//System.out.println("name: " + satName + " actualPos: " + satActPos);
			//System.out.println();
			ArrayList<String> names = new ArrayList<String>();
			names.add(satName);
			thisSat.setNames(names).setActualPosition(satActPos);
			
			for (Element x : tables) {
				if (x.className().equals("desktab")) {
					Element table = x.nextElementSibling();
					int i = 2;
					int rowspan = 0;
					WebsiteData.Satellite tempSat = new Satellite();
					for (Element entry: table.getElementsByTag("tbody").first().children()) {
						WebsiteData.Satellite.Transmitter transmitter = tempSat.new Transmitter();
//						System.out.println(entry.children().first());
						if(--rowspan > 0)
						{
							continue;
						}
						
						if(entry.children().hasAttr("rowspan"))
						{
							rowspan = Integer.parseInt(entry.children().attr("rowspan"));
//							System.out.println(rowspan);
						}
						
						if ((1 > i--) && (entry.children().first().className().equals("td-copyright") == false)) {
							String[] freqPol = entry.children().first().getElementsByTag("b").text().split(" ");
							Float freq = (float) 0;
							Character pol = null;
							String trans = null;
							String beam = null;
							String standard = null, modulation = null, fec = null;
							Integer symbolRate = 0;
							
							int beamIndex = 1;
							
							try {
								freq = Float.parseFloat(freqPol[0]);
							} catch (Exception e) {}
							try {
								pol = freqPol[1].charAt(0);
							} catch (Exception e) {}
							try {
								trans = entry.children().first().getElementsByAttributeValue("size", "1").first().text().split(" ")[1];
							} catch (IndexOutOfBoundsException e) {beamIndex=0;}
							catch (Exception e) {}
							//System.out.println("freq: " + freq + " polarisation: " + pol);
							//System.out.println("transponder: " + trans);
							try {
								beam = entry.children().first().getElementsByAttributeValue("size", "1").get(beamIndex).text();
							//	System.out.println("beam: " + beam);
							} catch(IndexOutOfBoundsException e) {}		
							catch (Exception e) {}
							
							try {
								String[] tmpTable = entry.children().get(1).getElementsByTag("font").text().split(" ");
								for(String info: tmpTable)
								{
									try
									{
										symbolRate = Integer.parseInt(info);
										continue;
									} catch(Exception e) {}
									
									if (info.indexOf("PSK")!= -1 || info.indexOf("ACM") != -1 || info.indexOf("QAM") != -1)
									{
										modulation = info;
										continue;
									} else if (info.indexOf("/") != -1)
									{
										fec = info;
										continue;
									} else {
										standard = info;
										continue;
									}
									
								}

							} catch(ArrayIndexOutOfBoundsException e){}
							//System.out.println("standard: " + standard + " modulation: " + modulation + " symbolRate: " + symbolRate + " fec: " + fec);
							//System.out.println("");



							transmitter.setFrequency(freq).setPolarisation(pol).setTransponder(trans).setBeam(beam)
							.setStandard(standard).setModulation(modulation).setSymbolRate(symbolRate).setFec(fec);
						}
						if(!Objects.equals(transmitter, null)){
							thisSat.transmitters.add(transmitter);
						}

					}
//					throw new InterruptedException(); // temporary, for checking only
				}
			}
			satellites.add(thisSat);
		}
		return satellites;
		//System.out.println(satellites);
	}

}
