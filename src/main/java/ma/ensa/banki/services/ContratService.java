package ma.ensa.banki.services;

import com.itextpdf.text.*;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import ma.ensa.banki.entities.Client;
import ma.ensa.banki.entities.Compte;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.PathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.time.LocalDateTime;


@Service
public class ContratService {


	@Value("classpath:/contrat/Contrat.txt")
	private Resource resource;

	public static String asString(Resource resource) {
		try (Reader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8)) {
			return FileCopyUtils.copyToString(reader);
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}


	public void createContrat(Compte compte, Client client) throws DocumentException, FileNotFoundException {

		Font fontTitre = new Font(FontFamily.TIMES_ROMAN, 28f, Font.UNDERLINE, BaseColor.RED);
		Font fontHeader = new Font(FontFamily.HELVETICA, 18f, Font.BOLD, BaseColor.BLACK);
		Font fontData = new Font(FontFamily.HELVETICA, 16f, Font.NORMAL, BaseColor.BLACK);
		Paragraph titre = new Paragraph("Contrat de création de compte.", fontTitre);
		titre.setAlignment(Element.ALIGN_CENTER);

		Paragraph space = new Paragraph(" ");

		LocalDateTime date = compte.getCreationDate();

		Paragraph ouverture = new Paragraph("L'agence bancaire " + compte.getCreationAgent().getAgence().getNom()
				+ " representée par l agent " + compte.getCreationAgent().getNom() + " " + compte.getCreationAgent().getPrenom()
				+ " s'engage à ouvrir un compte bancaire de type " + compte.getType() + ", au nom de la personne nommée " +
				"ci-dessous aux conditions stipulées dans le présent contrat et acceptées par ladite personne.", fontData);

		PdfPTable table = new PdfPTable(2);
		table.setWidthPercentage(100);

		Paragraph h1 = new Paragraph("Nom du titulaire", fontHeader);
		Paragraph h2 = new Paragraph(client.getNom() + " " + client.getPrenom(), fontData);

		table.addCell(new PdfPCell(h1));
		table.addCell(new PdfPCell(h2));

		h1 = new Paragraph("Cin ou Passeport", fontHeader);
		h2 = new Paragraph(client.getCin(), fontData);

		table.addCell(new PdfPCell(h1));
		table.addCell(new PdfPCell(h2));

		h1 = new Paragraph("Adresse", fontHeader);
		h2 = new Paragraph(client.getAdresse(), fontData);

		table.addCell(new PdfPCell(h1));
		table.addCell(new PdfPCell(h2));

		h1 = new Paragraph("Telephone", fontHeader);
		h2 = new Paragraph(client.getTelephone(), fontData);

		table.addCell(new PdfPCell(h1));
		table.addCell(new PdfPCell(h2));

		h1 = new Paragraph("Email", fontHeader);
		h2 = new Paragraph(client.getEmail(), fontData);

		table.addCell(new PdfPCell(h1));
		table.addCell(new PdfPCell(h2));

		h1 = new Paragraph("Type du compte", fontHeader);
		h2 = new Paragraph(compte.getType(), fontData);

		table.addCell(new PdfPCell(h1));
		table.addCell(new PdfPCell(h2));


		Paragraph compte_num = new Paragraph("Le numéro du compte auquel ce contrat s'applique est : "
				+ compte.getNumero(), fontData);

		Paragraph signature_titulaire = new Paragraph("Signature du titulaire du compte:", fontData);
		Paragraph signature_agent = new Paragraph("Signature de l'agent représentant la banque:", fontData);
		Paragraph dateContrat = new Paragraph("Date: " + date.toString(), fontHeader);

		String text = asString(resource);
		Paragraph conditions = new Paragraph(text, fontData);



		Document document = new Document();
		String filename = "CONTRAT_" + compte.getNumero() + "_" + date.withNano(0).toString().replace(':', '-') + ".pdf";
		Path path = FileSystems.getDefault().getPath("","src","main","resources","contrat","pdf",filename).toAbsolutePath();

		PdfWriter.getInstance(document, new FileOutputStream(String.valueOf(path)));

		document.open();

		document.add(titre);
		document.add(space);
		document.add(space);
		document.add(ouverture);
		document.add(space);
		document.add(space);
		document.add(table);
		document.add(space);
		document.add(compte_num);
		document.add(space);
		document.add(signature_titulaire);
		document.add(space);
		document.add(space);
		document.add(space);
		document.add(space);
		document.add(space);
		document.add(space);
		document.add(signature_agent);
		document.add(space);
		document.add(space);
		document.add(space);
		document.add(space);
		document.add(space);
		document.add(space);
		document.add(dateContrat);
		document.add(space);
		document.add(space);
		document.add(space);
		document.add(new Paragraph("Termes et condition", fontTitre));
		document.add(space);
		document.add(space);
		document.add(conditions);

		document.close();
	}


}
