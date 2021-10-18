package com.yhkim.developutility.common;

import com.yhkim.developutility.common.oid.OID;
import com.yhkim.developutility.common.oid.OIDMapping;
import lombok.RequiredArgsConstructor;
import org.bouncycastle.asn1.*;
import org.bouncycastle.util.Strings;
import org.bouncycastle.util.encoders.Hex;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Enumeration;

@Component
@RequiredArgsConstructor
public class CustomASN1Dump {
    private final OIDMapping oidMapping;

    private static final String  TAB = "    ";
    private static final int SAMPLE_SIZE = 32;

    public String dumpAsString(
            Object   obj,
            boolean  verbose) throws IOException {
        StringBuffer buf = new StringBuffer();

        if (obj instanceof ASN1Primitive)
        {
            _dumpAsString("", verbose, (ASN1Primitive)obj, buf);
        }
        else if (obj instanceof ASN1Encodable)
        {
            _dumpAsString("", verbose, ((ASN1Encodable)obj).toASN1Primitive(), buf);
        }
        else
        {
            return "unknown object type " + obj.toString();
        }

        return buf.toString();
    }

    public String dumpAsString(
            Object   obj,
            boolean  verbose,
            String indent) throws IOException {
        StringBuffer buf = new StringBuffer();

        if (obj instanceof ASN1Primitive)
        {
            _dumpAsString(indent, verbose, (ASN1Primitive)obj, buf);
        }
        else if (obj instanceof ASN1Encodable)
        {
            _dumpAsString(indent, verbose, ((ASN1Encodable)obj).toASN1Primitive(), buf);
        }
        else
        {
            return "unknown object type " + obj.toString();
        }

        return buf.toString();
    }

    /**
     * dump a DER object as a formatted string with indentation
     *
     * @param obj the ASN1Primitive to be dumped out.
     */
    void _dumpAsString(
            String      indent,
            boolean     verbose,
            ASN1Primitive obj,
            StringBuffer    buf) throws IOException {
        String nl = Strings.lineSeparator();
        if (obj instanceof ASN1Sequence)
        {
            Enumeration e = ((ASN1Sequence)obj).getObjects();
            String          tab = indent + TAB;

            buf.append(indent);
            if (obj instanceof BERSequence)
            {
                buf.append("BER Sequence");
            }
            else if (obj instanceof DERSequence)
            {
                buf.append("DER Sequence");
            }
            else
            {
                buf.append("Sequence");
            }

            buf.append(nl);

            while (e.hasMoreElements())
            {
                Object  o = e.nextElement();

                if (o == null || o.equals(DERNull.INSTANCE))
                {
                    buf.append(tab);
                    buf.append("NULL");
                    buf.append(nl);
                }
                else if (o instanceof ASN1Primitive)
                {
                    _dumpAsString(tab, verbose, (ASN1Primitive)o, buf);
                }
                else
                {
                    _dumpAsString(tab, verbose, ((ASN1Encodable)o).toASN1Primitive(), buf);
                }
            }
        }
        else if (obj instanceof ASN1TaggedObject)
        {
            String          tab = indent + TAB;

            buf.append(indent);
            if (obj instanceof BERTaggedObject)
            {
                buf.append("BER Tagged [");
            }
            else
            {
                buf.append("Tagged [");
            }

            ASN1TaggedObject o = (ASN1TaggedObject)obj;

            buf.append(Integer.toString(o.getTagNo()));
            buf.append(']');

            if (!o.isExplicit())
            {
                buf.append(" IMPLICIT ");
            }

            buf.append(nl);

            if (o.isEmpty())
            {
                buf.append(tab);
                buf.append("EMPTY");
                buf.append(nl);
            }
            else
            {
                _dumpAsString(tab, verbose, o.getObject(), buf);
            }
        }
        else if (obj instanceof ASN1Set)
        {
            Enumeration     e = ((ASN1Set)obj).getObjects();
            String          tab = indent + TAB;

            buf.append(indent);

            if (obj instanceof BERSet)
            {
                buf.append("BER Set");
            }
            else
            {
                buf.append("DER Set");
            }

            buf.append(nl);

            while (e.hasMoreElements())
            {
                Object  o = e.nextElement();

                if (o == null)
                {
                    buf.append(tab);
                    buf.append("NULL");
                    buf.append(nl);
                }
                else if (o instanceof ASN1Primitive)
                {
                    _dumpAsString(tab, verbose, (ASN1Primitive)o, buf);
                }
                else
                {
                    _dumpAsString(tab, verbose, ((ASN1Encodable)o).toASN1Primitive(), buf);
                }
            }
        }
        else if (obj instanceof ASN1OctetString)
        {
            ASN1OctetString oct = (ASN1OctetString)obj;
            ASN1InputStream asn1InputStream = new ASN1InputStream(oct.getOctets());
            Object object = null;
            try {
                indent += TAB;
                while ((object = asn1InputStream.readObject()) != null) {
                    buf.append(dumpAsString(object, true, indent));
                }
            } catch (IOException e) {
                if (obj instanceof BEROctetString)
                {
                    buf.append(indent + "BER Constructed Octet String" + "[" + oct.getOctets().length + "] ");
                }
                else
                {
                    buf.append(indent + "DER Octet String" + "[" + oct.getOctets().length + "] ");
                }
                if (verbose)
                {
                    buf.append(dumpBinaryDataAsString(indent, oct.getOctets()));
                }
                else
                {
                    buf.append(nl);
                }
            }
        }
        else if (obj instanceof ASN1ObjectIdentifier)
        {
            ASN1ObjectIdentifier identifier = (ASN1ObjectIdentifier) obj;
            OID oid = oidMapping.getOIDByIdentifier(identifier);
            buf.append(indent + "ObjectIdentifier (" + identifier.getId() + " - " + oid.getDomain() + ")" + " (" + oid.getComment() + ")" + nl);
        }
        else if (obj instanceof ASN1Boolean)
        {
            buf.append(indent + "Boolean(" + ((ASN1Boolean)obj).isTrue() + ")" + nl);
        }
        else if (obj instanceof ASN1Integer)
        {
            buf.append(indent + "Integer(" + ((ASN1Integer)obj).getValue() + ")" + nl);
        }
        else if (obj instanceof DERBitString)
        {
            DERBitString bt = (DERBitString)obj;
            buf.append(indent + "DER Bit String" + "[" + bt.getBytes().length + ", " + bt.getPadBits() + "] ");
            if (verbose)
            {
                buf.append(dumpBinaryDataAsString(indent, bt.getBytes()));
            }
            else
            {
                buf.append(nl);
            }
        }
        else if (obj instanceof DERIA5String)
        {
            buf.append(indent + "IA5String(" + ((DERIA5String)obj).getString() + ") " + nl);
        }
        else if (obj instanceof DERUTF8String)
        {
            buf.append(indent + "UTF8String(" + ((DERUTF8String)obj).getString() + ") " + nl);
        }
        else if (obj instanceof DERPrintableString)
        {
            buf.append(indent + "PrintableString(" + ((DERPrintableString)obj).getString() + ") " + nl);
        }
        else if (obj instanceof DERVisibleString)
        {
            buf.append(indent + "VisibleString(" + ((DERVisibleString)obj).getString() + ") " + nl);
        }
        else if (obj instanceof DERBMPString)
        {
            buf.append(indent + "BMPString(" + ((DERBMPString)obj).getString() + ") " + nl);
        }
        else if (obj instanceof DERT61String)
        {
            buf.append(indent + "T61String(" + ((DERT61String)obj).getString() + ") " + nl);
        }
        else if (obj instanceof DERGraphicString)
        {
            buf.append(indent + "GraphicString(" + ((DERGraphicString)obj).getString() + ") " + nl);
        }
        else if (obj instanceof DERVideotexString)
        {
            buf.append(indent + "VideotexString(" + ((DERVideotexString)obj).getString() + ") " + nl);
        }
        else if (obj instanceof ASN1UTCTime)
        {
            buf.append(indent + "UTCTime(" + ((ASN1UTCTime)obj).getTime() + ") " + nl);
        }
        else if (obj instanceof ASN1GeneralizedTime)
        {
            buf.append(indent + "GeneralizedTime(" + ((ASN1GeneralizedTime)obj).getTime() + ") " + nl);
        }
        else if (obj instanceof BERApplicationSpecific)
        {
            buf.append(outputApplicationSpecific("BER", indent, verbose, obj, nl));
        }
        else if (obj instanceof DERApplicationSpecific)
        {
            buf.append(outputApplicationSpecific("DER", indent, verbose, obj, nl));
        }
        else if (obj instanceof ASN1Enumerated)
        {
            ASN1Enumerated en = (ASN1Enumerated) obj;
            buf.append(indent + "DER Enumerated(" + en.getValue() + ")" + nl);
        }
        else if (obj instanceof DERExternal)
        {
            DERExternal ext = (DERExternal) obj;
            buf.append(indent + "External " + nl);
            String          tab = indent + TAB;
            if (ext.getDirectReference() != null)
            {
                buf.append(tab + "Direct Reference: " + ext.getDirectReference().getId() + nl);
            }
            if (ext.getIndirectReference() != null)
            {
                buf.append(tab + "Indirect Reference: " + ext.getIndirectReference().toString() + nl);
            }
            if (ext.getDataValueDescriptor() != null)
            {
                _dumpAsString(tab, verbose, ext.getDataValueDescriptor(), buf);
            }
            buf.append(tab + "Encoding: " + ext.getEncoding() + nl);
            _dumpAsString(tab, verbose, ext.getExternalContent(), buf);
        }
        else
        {
            buf.append(indent + obj.toString() + nl);
        }
    }

    private static String dumpBinaryDataAsString(String indent, byte[] bytes)
    {
        String nl = Strings.lineSeparator();
        StringBuffer buf = new StringBuffer();

        indent += TAB;

        buf.append(nl);
        for (int i = 0; i < bytes.length; i += SAMPLE_SIZE)
        {
            if (bytes.length - i > SAMPLE_SIZE)
            {
                buf.append(indent);
                buf.append(new String(Hex.encode(bytes, i, SAMPLE_SIZE)));
                buf.append(TAB);
                buf.append(calculateAscString(bytes, i, SAMPLE_SIZE));
                buf.append(nl);
            }
            else
            {
                buf.append(indent);
                buf.append(new String(Hex.encode(bytes, i, bytes.length - i)));
                for (int j = bytes.length - i; j != SAMPLE_SIZE; j++)
                {
                    buf.append("  ");
                }
                buf.append(TAB);
                buf.append(calculateAscString(bytes, i, bytes.length - i));
                buf.append(nl);
            }
        }

        return buf.toString();
    }

    private static String calculateAscString(byte[] bytes, int off, int len)
    {
        StringBuffer buf = new StringBuffer();

        for (int i = off; i != off + len; i++)
        {
            if (bytes[i] >= ' ' && bytes[i] <= '~')
            {
                buf.append((char)bytes[i]);
            }
        }

        return buf.toString();
    }

    private String outputApplicationSpecific(String type, String indent, boolean verbose, ASN1Primitive obj, String nl)
    {
        ASN1ApplicationSpecific app = ASN1ApplicationSpecific.getInstance(obj);
        StringBuffer buf = new StringBuffer();

        if (app.isConstructed())
        {
            try
            {
                ASN1Sequence s = ASN1Sequence.getInstance(app.getObject(BERTags.SEQUENCE));
                buf.append(indent + type + " ApplicationSpecific[" + app.getApplicationTag() + "]" + nl);
                for (Enumeration e = s.getObjects(); e.hasMoreElements();)
                {
                    _dumpAsString(indent + TAB, verbose, (ASN1Primitive)e.nextElement(), buf);
                }
            }
            catch (IOException e)
            {
                buf.append(e);
            }
            return buf.toString();
        }

        return indent + type + " ApplicationSpecific[" + app.getApplicationTag() + "] (" + new String(Hex.encode(app.getContents())) + ")" + nl;
    }
}
