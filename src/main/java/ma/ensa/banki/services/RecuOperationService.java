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
public class RecuOperationService {

	@Autowired
	CompteService compteService;

	@Autowired
	ClientService clientService;

	@Autowired
	AgenceService agenceService;

	@Autowired
	DeviseService deviseService;

	public void CreateRecu(Operation operation) throws DocumentException, URISyntaxException, IOException {
		Font fontTitre = new Font(FontFamily.TIMES_ROMAN, 30f, Font.UNDERLINE, BaseColor.RED);
		Font fontHeader = new Font(FontFamily.HELVETICA, 18f, Font.BOLD, BaseColor.BLACK);
		Font fontData = new Font(FontFamily.HELVETICA, 16f, Font.NORMAL, BaseColor.BLACK);
		Paragraph titre = new Paragraph("Reçu de operation.", fontTitre);
		titre.setAlignment(Element.ALIGN_CENTER);

		Paragraph space = new Paragraph(" ");

		LocalDateTime date = operation.getDate();
		Compte compte = compteService.getComptes(operation.getCompte().getId()).get(0);
		Client client = clientService.getClients(compte.getProprietaire().getId()).get(0);
		Agence agenceObj = agenceService.getAgences(client.getAgence().getId()).get(0);
		Devise devise = deviseService.getDevise(operation.getDevise().getId());

		Paragraph agence = new Paragraph("Agence : " + agenceObj.getNom(), fontHeader);
		Paragraph operationDate = new Paragraph("Date : " + date, fontHeader);


		PdfPTable table = new PdfPTable(2);
		table.setWidthPercentage(100);

		Paragraph h1 = new Paragraph("Client", fontHeader);
		Paragraph h2 = new Paragraph(client.getNom() + " " + client.getPrenom(), fontData);

		table.addCell(new PdfPCell(h1));
		table.addCell(new PdfPCell(h2));


		h1 = new Paragraph("N° de compte", fontHeader);
		h2 = new Paragraph(compte.getNumero(), fontData);

		table.addCell(new PdfPCell(h1));
		table.addCell(new PdfPCell(h2));


		h1 = new Paragraph("Type de l'opération", fontHeader);
		h2 = new Paragraph(operation.getType(), fontData);

		table.addCell(new PdfPCell(h1));
		table.addCell(new PdfPCell(h2));

		h1 = new Paragraph("Montant", fontHeader);
		h2 = new Paragraph(operation.getSommeEspece() + " " + devise.getCode(), fontData);

		table.addCell(new PdfPCell(h1));
		table.addCell(new PdfPCell(h2));

		String pathCachet = FileSystems.getDefault().getPath("","src","main","resources","recu","cachet.jpeg").toAbsolutePath().toString();
		Image img = Image.getInstance(pathCachet);
		img.scaleAbsolute(100F, 100F);
		img.setAlignment(Element.ALIGN_CENTER);


		String filename =  operation.getType() + "_" + compte.getNumero() + "_" + operation.getDate().withNano(0).toString().replace(':', '-') + ".pdf";
		Path path = FileSystems.getDefault().getPath("","src","main","resources","operations",filename).toAbsolutePath();

		Document document = new Document();
		PdfWriter.getInstance(document, new FileOutputStream(String.valueOf(path)));

		document.open();

		document.add(titre);
		document.add(space);
		document.add(space);
		document.add(agence);
		document.add(space);
		document.add(operationDate);
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
