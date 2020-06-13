package it.polito.dp2.BIB.sol3.resources;

import io.swagger.annotations.*;
import it.polito.dp2.BIB.sol3.service.BiblioService;
import it.polito.dp2.BIB.sol3.service.jaxb.Bookshelf;
import it.polito.dp2.BIB.sol3.service.jaxb.Bookshelves;
import it.polito.dp2.BIB.sol3.service.jaxb.Item;
import it.polito.dp2.BIB.sol3.service.jaxb.Items;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.math.BigInteger;
import java.net.URI;

@Path("/biblio")
@Api(value = "/biblio")
public class BookshelfResources {

    public UriInfo uriInfo;
    BiblioService service;

    public BookshelfResources(@Context UriInfo uriInfo) {
        this.uriInfo = uriInfo;
        this.service = new BiblioService(uriInfo);
    }

    @POST
    @Path("/bookshelves/{b_id}/items")
    @ApiOperation(value = "addItemInListInBookshelf", notes = "add a new item into the list", response = Item.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK", response = Item.class),
            @ApiResponse(code = 400, message = "Bad Request"), @ApiResponse(code = 403, message = "Action refused"),
            @ApiResponse(code = 404, message = "Item Not Found"),
            @ApiResponse(code = 409, message = "Conflict, item already in bookshelf"),})
    @Consumes({MediaType.TEXT_PLAIN})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response addItemInListInBookshelf(
            @ApiParam("The unique id of the bookshelf") @PathParam("b_id") BigInteger b_id, String tid_s)
            throws ClientErrorException,
            InternalServerErrorException {
        try {
            BigInteger tid = BigInteger.valueOf(Integer.parseInt(tid_s));
            Item item = service.addItemInListInBookshelf(b_id, tid);
            return Response.created(new URI(item.getSelf())).entity(item).build();

        } catch (NumberFormatException | BadRequestException e) {
            throw new BadRequestException();
        } catch (ForbiddenException e) {
            throw new ForbiddenException();
        } catch (NotFoundException e) {
            throw new NotFoundException();
        } catch (ClientErrorException e) {
            throw new ClientErrorException(e.getResponse().getStatus());
        } catch (Exception e) {
            throw new InternalServerErrorException();
        }
    }

    @GET
    @Path("/bookshelves/{b_id}/items")
    @ApiOperation(value = "getItemListOfBookshelf", notes = "search items into the list")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK", response = Items.class),
            @ApiResponse(code = 404, message = "Bookshelf Not Found"),})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Items getItemListOfBookshelf(@ApiParam("The unique id of the bookshelf") @PathParam("b_id") BigInteger b_id)
            throws NotFoundException, InternalServerErrorException {
        Items items;
        try {
            items = service.getItemListOfBookshelf(b_id);
        } catch (NotFoundException e) {
            throw new NotFoundException();
        } catch (Exception e) {
            throw new InternalServerErrorException();
        }

        return items;
    }

    @GET
    @Path("/bookshelves/{b_id}")
    @ApiOperation(value = "getBookshelf", notes = "retrieve a bookshelf")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK", response = Bookshelf.class),
            @ApiResponse(code = 404, message = "Bookshelf not Found"),})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Bookshelf getBookshelf(@ApiParam("The unique id of the bookshelf") @PathParam("b_id") BigInteger b_id)
            throws NotFoundException, InternalServerErrorException {
        Bookshelf bookshelf;

        try {
            bookshelf = service.getBookshelf(b_id);

        } catch (NotFoundException e) {
            throw new NotFoundException();
        } catch (Exception e) {
            throw new InternalServerErrorException();
        }

        return bookshelf;
    }

    @DELETE
    @Path("/bookshelves/{b_id}/items/{tid}")
    @ApiOperation(value = "removeItemInListInBookshelf", notes = "remove a item from the list")
    @ApiResponses(value = {@ApiResponse(code = 204, message = "No Content"),
            @ApiResponse(code = 400, message = "Invalid Bookshelf ID supplied or destroyed Bookshelf"),
            @ApiResponse(code = 404, message = "Item Not Found"),})
    public void removeItemInListInBookshelf(
            @ApiParam("The unique id of the bookshelf") @PathParam("b_id") BigInteger b_id,
            @ApiParam("The unique id of the item inside the list") @PathParam("tid") BigInteger tid)
            throws BadRequestException, NotFoundException, InternalServerErrorException {

        try {
            service.removeItemInListInBookshelf(b_id, tid);

        } catch (BadRequestException e) {
            throw new BadRequestException();
        } catch (NotFoundException e) {
            throw new NotFoundException();
        } catch (ClientErrorException e) {
            throw new ClientErrorException(e.getResponse().getStatus());
        } catch (Exception e) {
            throw new InternalServerErrorException();
        }
        return;
    }

    @POST
    @Path("/bookshelves")
    @ApiOperation(value = "createBookshelf", notes = "create a new bookshelf", response = Bookshelf.class)
    @ApiResponses(value = {@ApiResponse(code = 201, message = "Created", response = Bookshelf.class),
            @ApiResponse(code = 400, message = "Bad Request"),})
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response createBookshelf(Bookshelf bookshelf) throws BadRequestException, InternalServerErrorException {

        // Check request's body as soon as possible
        if (bookshelf == null)
            throw new BadRequestException();

        BigInteger b_id = BigInteger.ZERO;
        Bookshelf returnBookshelf = null;

        try {
            b_id = service.getNextId();
            returnBookshelf = service.createBookshelf(b_id, bookshelf);
            return Response.created(new URI(returnBookshelf.getSelf())).entity(returnBookshelf).build();
        } catch (BadRequestException e) {
            throw new BadRequestException();
        } catch (Exception e) {
            throw new InternalServerErrorException();
        }
    }

    @GET
    @Path("/bookshelves")
    @ApiOperation(value = "getBookshelves", notes = "search bookshelves")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK", response = Bookshelves.class),})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Bookshelves getBookshelves(
            @ApiParam("The prefix to be used for the search") @QueryParam("prefix") @DefaultValue("") String prefix)
            throws InternalServerErrorException {
        try {
            return service.getBookshelves(prefix);
        } catch (Exception e) {
            throw new InternalServerErrorException(e);
        }
    }

    @DELETE
    @Path("/bookshelves/{b_id}")
    @ApiOperation(value = "deleteBookshelf", notes = "delete a bookshelf")
    @ApiResponses(value = {@ApiResponse(code = 204, message = "OK"),
            @ApiResponse(code = 404, message = "Bookshelf not found"),})
    public void deleteBookshelf(
            @ApiParam("The unique id of the bookshelf to delete") @PathParam("b_id") BigInteger b_id)
            throws NotFoundException, InternalServerErrorException {
        try {
            service.deleteBookshelf(b_id);
        } catch (NotFoundException e) {
            throw new NotFoundException();
        } catch (Exception e) {
            throw new InternalServerErrorException();
        }
    }

    @GET
    @Path("/bookshelves/{b_id}/numberOfReads")
    @ApiOperation(value = "getNumberOfReads", notes = "read a read access counter to the bookshelf")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK", response = Integer.class),
            @ApiResponse(code = 404, message = "Bookshelf Not Found"),})
    @Produces(MediaType.TEXT_PLAIN)
    public Integer getNumberOfReads(@ApiParam("The unique id of the bookshelf") @PathParam("b_id") BigInteger b_id)
            throws NotFoundException, InternalServerErrorException {
        Integer i;
        try {
            i = service.getNumberOfReads(b_id);

        } catch (NotFoundException e) {
            throw new NotFoundException();
        } catch (Exception e) {
            throw new InternalServerErrorException();
        }

        return i;
    }


//    @POST
//    @Path("/bookshelves/{from}/{to}")
//    @ApiOperation(value = "copyFromBookshelfToBookshelf", notes = "search bookshelves")
//    @ApiResponses(value = { @ApiResponse(code = 200, message = "OK"),
//            @ApiResponse(code = 404, message = "Bookshelf not found"),
//            @ApiResponse(code = 403, message = "Too many items")})
//    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
//    public void copyFromBookshelfToBookshelf(
//            @ApiParam("The unique id of the item inside the list") @PathParam("from") @DefaultValue("") String from,
//            @ApiParam("The unique id of the item inside the list") @PathParam("to") @DefaultValue("") String to)
//            throws ForbiddenException,NotFoundException {
//        try {
//            service.copyBookshelfItems(from,to);
//        } catch(ForbiddenException e){
//            throw new ForbiddenException();
//        } catch(NotFoundException e){
//            throw new NotFoundException();
//        } catch (Exception e){
//            e.printStackTrace();
//            throw new InternalServerErrorException();
//        }
//    }

    @GET
    @Path("/bookshelves/{b_id}/items/{tid}")
    @ApiOperation(value = "getItemInListInBookshelf", notes = "retrieve an item in bookshelf")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK", response = Item.class),
            @ApiResponse(code = 400, message = "Invalid Bookshelf ID supplied or destroyed Bookshelf"),
            @ApiResponse(code = 404, message = "Item Not Found"),})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Item getItemInListInBookshelf(@ApiParam("The unique id of the bookshelf") @PathParam("b_id") BigInteger b_id,
                                         @ApiParam("The unique id of the item inside the list") @PathParam("tid") BigInteger tid)
            throws BadRequestException, NotFoundException, InternalServerErrorException {
        Item item;
        try {
            item = service.getItemInListInBookshelf(b_id, tid);
        } catch (BadRequestException e) {
            throw new BadRequestException();
        } catch (NotFoundException e) {
            throw new NotFoundException();
        } catch (Exception e) {
            throw new InternalServerErrorException();
        }

        return item;
    }

}