package com.epam.cdp.web.converter;

import com.epam.cdp.model.Ticket;
import com.epam.cdp.util.FileUtils;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractGenericHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

public class PdfHttpMessageConverter extends AbstractGenericHttpMessageConverter<Object> {

    public PdfHttpMessageConverter() {
        super(new MediaType("application", "pdf"));
    }

    @Override
    protected void writeInternal(Object object, Type type, HttpOutputMessage httpOutputMessage) throws IOException, HttpMessageNotWritableException {
        FileUtils.writeInPdfToStream(object, httpOutputMessage.getBody());
    }

    @Override
    public boolean canWrite(Type type, Class<?> contextClass, MediaType mediaType) {
        if (super.canWrite(mediaType)) {
            if (type.equals(List.class)) {
                Type actualTypeArgument = ((ParameterizedType) type).getActualTypeArguments()[0];
                return actualTypeArgument.equals(Ticket.class);
            } else {
                return type.equals(Ticket.class);
            }
        }
        return false;
    }

    @Override
    protected boolean supports(Class<?> aClass) {
        return true;
    }

    @Override
    protected Object readInternal(Class<?> aClass, HttpInputMessage httpInputMessage) throws IOException, HttpMessageNotReadableException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object read(Type type, Class<?> aClass, HttpInputMessage httpInputMessage) throws IOException, HttpMessageNotReadableException {
        throw new UnsupportedOperationException();
    }
}