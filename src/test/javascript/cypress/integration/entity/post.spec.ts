import {
  entityTableSelector,
  entityDetailsButtonSelector,
  entityDetailsBackButtonSelector,
  entityCreateButtonSelector,
  entityCreateSaveButtonSelector,
  entityEditButtonSelector,
  entityDeleteButtonSelector,
  entityConfirmDeleteButtonSelector,
} from '../../support/entity';

describe('Post e2e test', () => {
  let startingEntitiesCount = 0;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.clearCookie('SESSION');
    cy.clearCookies();
    cy.intercept('GET', '/api/posts*').as('entitiesRequest');
    cy.visit('');
    cy.login('admin', 'admin');
    cy.clickOnEntityMenuItem('post');
    cy.wait('@entitiesRequest').then(({ request, response }) => (startingEntitiesCount = response.body.length));
    cy.visit('/');
  });

  it('should load Posts', () => {
    cy.intercept('GET', '/api/posts*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('post');
    cy.wait('@entitiesRequest');
    cy.getEntityHeading('Post').should('exist');
    if (startingEntitiesCount === 0) {
      cy.get(entityTableSelector).should('not.exist');
    } else {
      cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount);
    }
    cy.visit('/');
  });

  it('should load details Post page', () => {
    cy.intercept('GET', '/api/posts*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('post');
    cy.wait('@entitiesRequest');
    if (startingEntitiesCount > 0) {
      cy.get(entityDetailsButtonSelector).first().click({ force: true });
      cy.getEntityDetailsHeading('post');
      cy.get(entityDetailsBackButtonSelector).should('exist');
    }
    cy.visit('/');
  });

  it('should load create Post page', () => {
    cy.intercept('GET', '/api/posts*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('post');
    cy.wait('@entitiesRequest');
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('Post');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.visit('/');
  });

  it('should load edit Post page', () => {
    cy.intercept('GET', '/api/posts*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('post');
    cy.wait('@entitiesRequest');
    if (startingEntitiesCount > 0) {
      cy.get(entityEditButtonSelector).first().click({ force: true });
      cy.getEntityCreateUpdateHeading('Post');
      cy.get(entityCreateSaveButtonSelector).should('exist');
    }
    cy.visit('/');
  });

  /* this test is commented because it contains required relationships
  it('should create an instance of Post', () => {
    cy.intercept('GET', '/api/posts*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('post');
    cy.wait('@entitiesRequest')
      .then(({ request, response }) => startingEntitiesCount = response.body.length);
    cy.get(entityCreateButtonSelector).click({force: true});
    cy.getEntityCreateUpdateHeading('Post');

    cy.get(`[data-cy="creationDate"]`).type('2021-05-10T10:15').invoke('val').should('equal', '2021-05-10T10:15');


    cy.get(`[data-cy="publicationDate"]`).type('2021-05-11T02:24').invoke('val').should('equal', '2021-05-11T02:24');


    cy.get(`[data-cy="headline"]`).type('world-class Hat', { force: true }).invoke('val').should('match', new RegExp('world-class Hat'));


    cy.get(`[data-cy="leadtext"]`).type('help-desk Tools Loan', { force: true }).invoke('val').should('match', new RegExp('help-desk Tools Loan'));


    cy.get(`[data-cy="bodytext"]`).type('magenta mission-critical New', { force: true }).invoke('val').should('match', new RegExp('magenta mission-critical New'));


    cy.get(`[data-cy="quote"]`).type('revolutionize Markets', { force: true }).invoke('val').should('match', new RegExp('revolutionize Markets'));


    cy.get(`[data-cy="conclusion"]`).type('Dominica', { force: true }).invoke('val').should('match', new RegExp('Dominica'));


    cy.get(`[data-cy="linkText"]`).type('Designer Handmade', { force: true }).invoke('val').should('match', new RegExp('Designer Handmade'));


    cy.get(`[data-cy="linkURL"]`).type('compress', { force: true }).invoke('val').should('match', new RegExp('compress'));


    cy.setFieldImageAsBytesOfEntity('image', 'integration-test.png', 'image/png');

    cy.setFieldSelectToLastOfEntity('appuser');

    cy.setFieldSelectToLastOfEntity('blog');

    cy.get(entityCreateSaveButtonSelector).click({force: true});
    cy.scrollTo('top', {ensureScrollable: false});
    cy.get(entityCreateSaveButtonSelector).should('not.exist');
    cy.intercept('GET', '/api/posts*').as('entitiesRequestAfterCreate');
    cy.visit('/');
    cy.clickOnEntityMenuItem('post');
    cy.wait('@entitiesRequestAfterCreate');
    cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount + 1);
    cy.visit('/');
  });
  */

  /* this test is commented because it contains required relationships
  it('should delete last instance of Post', () => {
    cy.intercept('GET', '/api/posts*').as('entitiesRequest');
    cy.intercept('DELETE', '/api/posts/*').as('deleteEntityRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('post');
    cy.wait('@entitiesRequest').then(({ request, response }) => {
      startingEntitiesCount = response.body.length;
      if (startingEntitiesCount > 0) {
        cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount);
        cy.get(entityDeleteButtonSelector).last().click({force: true});
        cy.getEntityDeleteDialogHeading('post').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({force: true});
        cy.wait('@deleteEntityRequest');
        cy.intercept('GET', '/api/posts*').as('entitiesRequestAfterDelete');
        cy.visit('/');
        cy.clickOnEntityMenuItem('post');
        cy.wait('@entitiesRequestAfterDelete');
        cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount - 1);
      }
      cy.visit('/');
    });
  });
  */
});
