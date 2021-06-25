package ma.ensa.banki.services;

import com.itextpdf.text.*;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import ma.ensa.banki.entities.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.PathResource;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

@Service
public class RecuVirementService {

	@Autowired
	CompteService compteService;

	@Autowired
	ClientService clientService;

	@Autowired
	AgenceService agenceService;

	@Autowired
	DeviseService deviseService;

	public void CreateRecu(Virement virement) throws DocumentException, URISyntaxException, IOException {
		Font fontTitre = new Font(FontFamily.TIMES_ROMAN, 30f, Font.UNDERLINE, BaseColor.RED);
		Font fontHeader = new Font(FontFamily.HELVETICA, 18f, Font.BOLD, BaseColor.BLACK);
		Font fontData = new Font(FontFamily.HELVETICA, 16f, Font.NORMAL, BaseColor.BLACK);
		Paragraph titre = new Paragraph("Reçu de virement.", fontTitre);
		titre.setAlignment(Element.ALIGN_CENTER);

		Paragraph space = new Paragraph(" ");

		LocalDateTime date = virement.getDate();
		Compte debiteur = compteService.getComptes(virement.getDebiteur().getId()).get(0);
		Compte creancier = compteService.getComptes(virement.getCreancier().getId()).get(0);
		Client client = clientService.getClients(debiteur.getProprietaire().getId()).get(0);
		Agence agenceDebiteur = agenceService.getAgences(client.getAgence().getId()).get(0);
		Devise devise = deviseService.getDevise(debiteur.getDevise().getId());

		Paragraph agence = new Paragraph("Agence : " + agenceDebiteur.getNom(), fontHeader);
		Paragraph virementDate = new Paragraph("Date : " + date, fontHeader);


		PdfPTable table = new PdfPTable(2);
		table.setWidthPercentage(100);

		Paragraph h1 = new Paragraph("Client", fontHeader);
		Paragraph h2 = new Paragraph(client.getNom() + " " + client.getPrenom(), fontData);

		table.addCell(new PdfPCell(h1));
		table.addCell(new PdfPCell(h2));

		h1 = new Paragraph("Cin ou Passeport", fontHeader);
		h2 = new Paragraph(client.getCin(), fontData);

		table.addCell(new PdfPCell(h1));
		table.addCell(new PdfPCell(h2));

		h1 = new Paragraph("N° de compte", fontHeader);
		h2 = new Paragraph(debiteur.getNumero(), fontData);

		table.addCell(new PdfPCell(h1));
		table.addCell(new PdfPCell(h2));


		h1 = new Paragraph("N° de compte du bénificiare", fontHeader);
		h2 = new Paragraph(creancier.getNumero(), fontData);

		table.addCell(new PdfPCell(h1));
		table.addCell(new PdfPCell(h2));

		h1 = new Paragraph("Montant", fontHeader);
		h2 = new Paragraph(virement.getSommeEnv() + " " + devise.getCode(), fontData);

		table.addCell(new PdfPCell(h1));
		table.addCell(new PdfPCell(h2));

		String pathCachet = FileSystems.getDefault().getPath("","src","main","resources","recu","cachet.jpeg").toAbsolutePath().toString();
		Image img = Image.getInstance(pathCachet);
		img.scaleAbsolute(100F, 100F);
		img.setAlignment(Element.ALIGN_CENTER);

		Document document = new Document();

		String filename = "virement_" + debiteur.getNumero() + "_" + date.withNano(0).toString().replace(':', '-') + ".pdf";
		Path path = FileSystems.getDefault().getPath("","src","main","resources","virements",filename).toAbsolutePath();

		PdfWriter.getInstance(document, new FileOutputStream(String.valueOf(path)));

		document.open();

		document.add(titre);
		document.add(space);
		document.add(space);
		document.add(agence);
		document.add(space);
		document.add(virementDate);
		document.add(space);
		document.add(space);
		document.add(space);
		document.add(table);
		document.add(space);
		document.add(space);
		document.add(space);
		document.add(img);
		document.close();
	}

}
