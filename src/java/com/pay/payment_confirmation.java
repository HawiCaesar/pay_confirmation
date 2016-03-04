/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pay;

import java.beans.Statement;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Enumeration;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.HttpsURLConnection;
import javax.servlet.ServletInputStream;

/**
 *
 * @author hawi
 */
public class payment_confirmation extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     * @throws java.sql.SQLException
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {

        //exchange of certificates
        //try {

            //TrustAllCerts();

            ServletInputStream i = request.getInputStream();

            int c = 0;
            String xmlrpc = "";
            while ((c = i.read()) != -1) {
                xmlrpc += (char) c;
            }

            int startTag = xmlrpc.indexOf("<TransID>");
            int endTag = xmlrpc.indexOf("</TransID>");
            String parameter = xmlrpc.substring(startTag, endTag).replaceAll("<TransID>", "");

            //connect to databse & save to database, the data
//            Connection conn = null;
//
//            try {
//                Class.forName("com.mysql.jdbc.Driver");

//                conn = DriverManager.getConnection("jdbc:mysql://1.1.1.1:3306/YOURDATABASE", "YOUR_USERNAME", "YOUR_PASSWORD");
//
//                PreparedStatement pstmt = conn.prepareStatement("INSERT INTO TABLE (text) values(?)");
//                pstmt.setString(1, parameter);
//
//                pstmt.executeUpdate();
//                pstmt.close();
//
//            } catch (ClassNotFoundException ex) {
//                Logger.getLogger(payment_confirmation.class.getName()).log(Level.SEVERE, null, ex);
//            } catch (SQLException se) {
//                // log exception
//                throw se;
//            }

            //set repsonse type & structure response
            response.setContentType("text/xml;charset=UTF-8");
            String resultxml = "<?xml version=\"1.0\" encoding=\"utf-8\" ?>\n";
            resultxml += "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:c2b=\"http://cps.huawei.com/cpsinterface/c2bpayment\">\n";
            resultxml += "<soapenv:Header/>\n";
            resultxml += "<soapenv:Body>\n";
            resultxml += "<c2b:C2BPaymentConfirmationResult>C2B Payment Transaction " + parameter + " result received.</c2b:C2BPaymentConfirmationResult>\n";
            resultxml += "</soapenv:Body>\n";
            resultxml += "</soapenv:Envelope>\n";

            //send out responese
            response.getWriter().println(resultxml);

        //} catch (NoSuchAlgorithmException | KeyManagementException | KeyStoreException | FileNotFoundException | CertificateException ex) {
         //   Logger.getLogger(payment_confirmation.class.getName()).log(Level.SEVERE, null, ex);
        //}

    }

    private static void TrustAllCerts() throws java.security.NoSuchAlgorithmException, java.security.KeyManagementException, KeyStoreException, FileNotFoundException, IOException, CertificateException {

        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");

        TrustManager[] trustAllCerts = new TrustManager[]{
            new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] xcs, String string) throws CertificateException {
                    //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    // new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    return null;
                }

                @Override
                public void checkServerTrusted(X509Certificate[] certs, String authType) throws CertificateException {
                    //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    System.out.println("authType is " + authType);
                    System.out.println("cert issuers");
                    for (int i = 0; i < certs.length; i++) {
                        System.out.println("\t" + certs[i].getIssuerX500Principal().getName());
                        System.out.println("\t" + certs[i].getIssuerDN().getName());
                    }
                }
            }
        };

        SSLContext sc = SSLContext.getInstance("SSL");
        // Create empty HostnameVerifier
        HostnameVerifier hv = new HostnameVerifier() {
            public boolean verify(String arg0, SSLSession arg1) {
                return true;
            }

        };

        String keyStorePath = ".";
        String keyStorePass = " bob_pfx";
        String certAlias = "le-b76a47a5-5e07-487f-b6f9-0f944b4b4d51";
        String certPass = "";
        boolean isKeyStore = true;
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");

        try {
            KeyStore certStore = KeyStore.getInstance("PKCS12");
            if (!isKeyStore) {
                File certFile = new File(keyStorePath);
                InputStream keyInput = new FileInputStream(certFile);
                certStore.load(keyInput, certPass.toCharArray());
                keyInput.close();
            } else {

                FileInputStream kfis = new FileInputStream("bob_pfx.pfx");
                KeyStore keyStore = KeyStore.getInstance("PKCS12");
                keyStore.load(kfis, keyStorePass.toCharArray());

                // Get certificate
                Certificate cert = keyStore.getCertificate(certAlias);
                certStore = KeyStore.getInstance(KeyStore.getDefaultType());
                certStore.load(null, null);

                certStore.setCertificateEntry(certAlias, cert);
                for (Enumeration<String> e = certStore.aliases();
                        e.hasMoreElements();) {
                    System.out.println(e.nextElement());
                }
            }
            keyManagerFactory.init(certStore, certPass.toCharArray());
        } catch (KeyStoreException ke) {
            System.out.println(date.toString() + "|Error loading client certificate: " + ke.toString());
        } catch (UnrecoverableKeyException ue) {
            System.out.println(date.toString() + "|Error loading client certificate: " + ue.toString());
        } catch (CertificateException ce) {
            System.out.println(date.toString() + "|Error loading client certificate: " + ce.toString());
        } catch (NoSuchAlgorithmException ne) {
            System.out.println(date.toString() + "|Error loading client certificate: " + ne.toString());
        } catch (IOException io) {
            System.out.println(date.toString() + "|Error loading client certificate: " + io.toString());
        } finally {
            sc.init(keyManagerFactory.getKeyManagers(), trustAllCerts, new java.security.SecureRandom());
        }

        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        HttpsURLConnection.setDefaultHostnameVerifier(hv);

    }

}

// <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
/**
 * Handles the HTTP <code>GET</code> method.
 *
 * @param request servlet request
 * @param response servlet response
 * @throws ServletException if a servlet-specific error occurs
 * @throws IOException if an I/O error occurs
 */
//@Override
//        protected void doGet(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//        processRequest(request, response);
//    }
/**
 * Handles the HTTP <code>POST</code> method.
 *
 * @param request servlet request
 * @param response servlet response
 * @throws ServletException if a servlet-specific error occurs
 * @throws IOException if an I/O error occurs
 */
//    @Override
//        protected void doPost(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//        processRequest(request, response);
//    }
/**
 * Returns a short description of the servlet.
 *
 * @return a String containing servlet description
 */
//    @Override
//        public String getServletInfo() {
//        return "Short description";
//    }// </editor-fold>

