package com.koinRest;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.koinRest.dao.CidadeDAO;
import com.koinRest.entity.Cidade;
import com.koinRest.entity.CidadeProxima;
import com.koinRest.entity.SaidaJSON;

/**
 * Example resource class hosted at the URI path "/lista"
 */
@Path("/lista")
public class DistanciaResource {

	/**
	 * Ex: http://localhost:8080/koinTest/REST/lista/1&3
	 */
	@GET
	@Path("{cidade1}&{cidade2}")
	@Produces(MediaType.APPLICATION_JSON)
	public SaidaJSON getIt(@PathParam("cidade1") int cidade1,
			@PathParam("cidade2") int cidade2) {
		List<String> lista = new ArrayList<String>();
		Cidade local1 = new CidadeDAO().buscarCidade(cidade1);
		Cidade local2 = new CidadeDAO().buscarCidade(cidade2);
		SaidaJSON resultado = new SaidaJSON();
		Document document;
		URL url;
		try {
			url = new URL(
					"http://maps.googleapis.com/maps/api/directions/xml?sensor=true&"
							+ "origin=" + local1.getLatitude() + ","
							+ local1.getLongitude() + "&destination="
							+ local2.getLatitude() + ","
							+ local2.getLongitude() + "&sensor=false");

			document = getDocumento(url);
			lista = analisaXml(document);

			resultado.setOrigem(local1.getNome());
			resultado.setDestino(local2.getNome());
			resultado.setDistancia(lista.get(0));
			resultado.setStatus(lista.get(1));

		} catch (MalformedURLException | DocumentException e) {
			e.printStackTrace();
		}
		return resultado;
	}

	@SuppressWarnings("rawtypes")
	public List analisaXml(Document document) {
		List<String> elementos = new ArrayList<String>();
		List list = document
				.selectNodes("//DirectionsResponse/route/leg/distance/text");
		List codStatus = document.selectNodes("//DirectionsResponse/status");

		Element element = (Element) list.get(list.size() - 1);
		Element status = (Element) codStatus.get(list.size() - 1);
		elementos.add(element.getText());
		elementos.add(status.getText());

		return elementos;
	}

			
	public Document getDocumento(URL url) throws DocumentException {
		SAXReader reader = new SAXReader();
		Document document = reader.read(url);
		return document;
	}

	/**
	 * Ex: http://localhost:8080/koinTest/REST/lista/next/1
	 */
	@GET
	@Path("next/{cidade}")
	@Produces(MediaType.APPLICATION_JSON)
	public CidadeProxima getIt(@PathParam("cidade") int id) {
		List<Cidade> cidades = new ArrayList<Cidade>();
		Map<Integer, Cidade> dist = new HashMap<Integer, Cidade>();
		CidadeProxima cp = new CidadeProxima();
		Document document;
		List<String> lista;
		URL url;
		Cidade local = new CidadeDAO().buscarCidade(id);
		cidades = new CidadeDAO().buscarTodas();

		for (Cidade cidade : cidades) {
				
			if (!local.equals(cidade)) {
				try {
					url = new URL(
							"http://maps.googleapis.com/maps/api/directions/xml?sensor=true&"
									+ "origin=" + local.getLatitude() + ","
									+ local.getLongitude() + "&destination="
									+ cidade.getLatitude() + ","
									+ cidade.getLongitude() + "&sensor=false");

					document = getDocumento(url);
					lista = analisaXml(document);
					String valor = lista.get(0).replace(" km", "")
							.replace(" m", "");
					int distancia;
					if (valor.indexOf(".") >= 0) {
						distancia = (int) Math.round(Double.valueOf(valor));
					} else {
						distancia = (int) (Double.valueOf(valor.replace(",",
								".")) * 1000);
					}

					System.out.println(cidade.getNome() + ": " + distancia	+ " km");
					dist.put(distancia, cidade);

				} catch (MalformedURLException | DocumentException e) {
					e.printStackTrace();
				}
			}
		}
		Map<Integer, Cidade> ordem = new TreeMap<Integer, Cidade>(dist);
		List ordenada = new ArrayList<Integer>();
		Iterator it = ordem.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pairs = (Map.Entry) it.next();
			System.out.println(pairs.getKey() + " = " + pairs.getValue());
			ordenada.add(pairs.getKey());
			it.remove();
		}
		cp.setOrigem(local.getNome());
		cp.setProxima(dist.get(ordenada.get(0)).getNome());
		cp.setDist(ordenada.get(0).toString());
		cp.setProxima2(dist.get(ordenada.get(1)).getNome());
		cp.setDist2(ordenada.get(1).toString());

		return cp;
	}

}