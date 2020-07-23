[DEPRECATED] This approach to integrating MPESA is no longer supported. Please follow MPESA's up to date documentation [here](https://developer.safaricom.co.ke/)

## MPESA API Integration - Payment Confirmation

This is a simple guide on what you need and what to get ready for when integrating MPESA with your payment option.

This guide uses the "Developers guide_Paybill&Buygoods Validation & Confirmation_v0.3" provided by MPESA support

You can find it here http://www.safaricom.co.ke/business/corporate/m-pesa-payments-services/m-pesa-api

Note: The scope of this guide is for businesses looking to allow customers to pay via MPESA for online web applications

## Prerequisites

1) System Architecture - This is a blueprint or road map of how you will enable the customers to pay via MPESA. 

It should describe how the customer initiates the payment right through to the end where your web application confirms the customer's paymemt and allow the customer to download a receipt

2) An Actual Domain of your working web application. This by default means your web application is on a live server.

3) Knowledge on Mutual Authentication and how to apply it to your system architecture. At this point you may have a rough idea on what technology you are going to use to implement mutual authentication

4) The Documentation: have them readily accessible

    - "Developers guide_Paybill&Buygoods Validation & Confirmation_v0.3" page 11-22
    - "SSL Guide"
    - "WSDL_V1.0"

5) SOAP UI - an open software used to test and interact with web API's services

## Check out this simple tutorial I made,

https://hawihustle.wordpress.com/2016/05/23/mpesa-api-integration-payment-confirmation/
