package com.example.demo.service;

import com.example.demo.model.Profile;
import com.example.demo.model.Template;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

@Service
public class CardRenderService {
    private final CodeImageService codeImageService;

    public CardRenderService(CodeImageService codeImageService) {
        this.codeImageService = codeImageService;
    }

    public byte[] pdf(Profile profile) {
        try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            PdfWriter writer = new PdfWriter(output);
            PdfDocument pdfDocument = new PdfDocument(writer);
            Document document = new Document(pdfDocument, PageSize.A6);
            document.setMargins(18, 18, 18, 18);

            Template template = profile.getTemplate();
            DeviceRgb primary = color(template == null ? "#1d4ed8" : template.getPrimaryColor());
            String organization = template == null ? "ID Card" : template.getOrganizationName();

            document.add(new Paragraph(organization == null ? "ID Card" : organization)
                    .setBold()
                    .setFontSize(16)
                    .setFontColor(ColorConstants.WHITE)
                    .setBackgroundColor(primary)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setPadding(10));

            document.add(new Paragraph(profile.getFullName()).setBold().setFontSize(18).setTextAlignment(TextAlignment.CENTER));
            document.add(new Paragraph(profile.getType() + " / " + profile.getTitle()).setTextAlignment(TextAlignment.CENTER));
            document.add(detailTable(profile));
            document.add(new Image(ImageDataFactory.create(codeImageService.qrCode(profile))).setWidth(90).setHorizontalAlignment(com.itextpdf.layout.properties.HorizontalAlignment.CENTER));
            document.add(new Image(ImageDataFactory.create(codeImageService.barcode(profile))).setWidth(180).setHorizontalAlignment(com.itextpdf.layout.properties.HorizontalAlignment.CENTER));
            document.close();
            return output.toByteArray();
        } catch (Exception ex) {
            throw new IllegalStateException("Could not render PDF", ex);
        }
    }

    private Table detailTable(Profile profile) {
        Table table = new Table(UnitValue.createPercentArray(new float[]{35, 65}));
        table.setWidth(UnitValue.createPercentValue(100));
        addRow(table, "Reg No", profile.getRegistrationNumber());
        addRow(table, "Department", profile.getDepartment());
        addRow(table, "Email", profile.getEmail());
        addRow(table, "Phone", profile.getPhone());
        addRow(table, "Blood", profile.getBloodGroup());
        addRow(table, "Expires", profile.getExpiryDate() == null ? "" : profile.getExpiryDate().toString());
        return table;
    }

    private void addRow(Table table, String label, String value) {
        table.addCell(cell(label, true));
        table.addCell(cell(value == null ? "" : value, false));
    }

    private Cell cell(String text, boolean label) {
        Paragraph paragraph = new Paragraph(text).setFontSize(9);
        if (label) {
            paragraph.setBold();
        }
        return new Cell()
                .add(paragraph)
                .setBorder(new SolidBorder(ColorConstants.LIGHT_GRAY, 0.5f))
                .setPadding(4);
    }

    private DeviceRgb color(String hex) {
        String value = hex == null ? "#1d4ed8" : hex.replace("#", "");
        return new DeviceRgb(
                Integer.parseInt(value.substring(0, 2), 16),
                Integer.parseInt(value.substring(2, 4), 16),
                Integer.parseInt(value.substring(4, 6), 16));
    }
}
